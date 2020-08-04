package com.ovle.rll3.model.ecs.component.dto

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.*
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.component.util.Mappers.move
import com.ovle.rll3.model.ecs.component.util.Mappers.position
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
    val targetFilter: TaskTargetFilter? = null, //todo use
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
    oneTimeAction = ::moveTaskAction,
    successCondition = ::isNearPositionCondition
)

val moveToTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    oneTimeAction = ::moveTaskAction,
    successCondition = ::isAtPositionCondition
)

val attackTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    everyTurnAction = ::attackAction,
    successCondition = ::isTargetDeadCondition,
    failCondition = { e, t -> !isNearEntityCondition(e, t) }
)

fun moveTaskAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.PositionTarget  //todo
    send(Event.EntitySetMoveTarget(e, t.position))
}

fun attackAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.EntityTarget  //todo
    val st = TemplatesRegistry.skillTemplates["attack"] //todo
    send(Event.EntityUseSkill(e, t.entity, st!!))
}

fun isNearEntityCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.EntityTarget
    return e[position]!!.gridPosition.isNear(t.entity[position]!!.gridPosition)
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