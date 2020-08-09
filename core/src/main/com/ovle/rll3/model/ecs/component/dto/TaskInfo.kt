package com.ovle.rll3.model.ecs.component.dto

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.*
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.advanced.LivingComponent
import com.ovle.rll3.model.ecs.component.basic.ResourceComponent
import com.ovle.rll3.model.ecs.component.basic.SourceComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.has
import com.ovle.rll3.model.ecs.entity.anyTaskPerformer
import com.ovle.rll3.model.template.TemplatesRegistry
import ktx.ashley.get
import java.io.Serializable


data class TaskInfo(
    val template: TaskTemplate,
    val performer: Entity,   //todo multiple ?
    val target: TaskTarget,
    var started: Boolean = false
): Serializable {

}

data class TaskTemplate (
    val performerFilter: TaskPerformerFilter,
    val targetFilter: TaskTargetFilter,
    val oneTimeAction: TaskAction? = null,
    val everyTurnAction: TaskAction? = null,
    val successCondition: SuccessCondition,
    val failCondition: FailCondition? = null
)

sealed class TaskTarget {
    class EntityTarget(val entity: Entity): TaskTarget()
    class PositionTarget(val position: GridPoint2): TaskTarget()
    class AreaTarget(val area: Array<GridPoint2>): TaskTarget()
}

val moveNearToTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = ::isEntityCondition,
    oneTimeAction = ::moveTaskAction,
    successCondition = ::isNearPositionCondition
)

val moveToTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = ::isPositionCondition,
    oneTimeAction = ::moveTaskAction,
    successCondition = ::isAtPositionCondition
)

val attackTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = ::isLivingEntityCondition,
    everyTurnAction = ::attackAction,
    successCondition = ::isTargetDeadCondition,
    failCondition = { e, t -> !isNearEntityCondition(e, t) }
)

val gatherTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = ::isSourceEntityCondition,
    everyTurnAction = ::gatherAction,
    successCondition = { e, t -> !isTargetExistsCondition(e, t) },
    failCondition = { e, t -> !isNearEntityCondition(e, t) }
)

fun moveTaskAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.PositionTarget  //todo
    send(Event.EntitySetMoveTarget(e, t.position))
}

fun gatherAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.EntityTarget  //todo
    val st = TemplatesRegistry.skillTemplates["gather"] //todo
    send(Event.EntityUseSkill(e, t.entity, st!!))
}

fun attackAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.EntityTarget  //todo
    val st = TemplatesRegistry.skillTemplates["attack"] //todo
    send(Event.EntityUseSkill(e, t.entity, st!!))
}

fun isNearEntityCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.EntityTarget
    val targetPosition = t.entity[position]?.gridPosition ?: return false
    return e[position]!!.gridPosition.isNear(targetPosition)
}

fun isNearPositionCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.PositionTarget
    return e[position]!!.gridPosition.isNear(t.position)
}

fun isAtPositionCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.PositionTarget
    return e[position]!!.gridPosition == t.position
}

fun isTargetDeadCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.EntityTarget
    return t.entity[living]!!.isDead
}

fun isTargetExistsCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.EntityTarget
    return !t.entity.isScheduledForRemoval
}

fun isPositionCondition(t: TaskTarget): Boolean = t is TaskTarget.PositionTarget
fun isEntityCondition(t: TaskTarget): Boolean = t is TaskTarget.EntityTarget

fun isLivingEntityCondition(t: TaskTarget): Boolean =
    t is TaskTarget.EntityTarget &&
    t.entity.has<LivingComponent>()

fun isSourceEntityCondition(t: TaskTarget): Boolean =
    t is TaskTarget.EntityTarget &&
    t.entity.has<SourceComponent>()

fun isResourceEntityCondition(t: TaskTarget): Boolean =
    t is TaskTarget.EntityTarget &&
    t.entity.has<ResourceComponent>()
