package com.ovle.rll3.model.ecs.component.dto

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.FailCondition
import com.ovle.rll3.SuccessCondition
import com.ovle.rll3.TaskAction
import com.ovle.rll3.TaskPerformerFilter
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers.move
import com.ovle.rll3.model.ecs.entity.anyTaskPerformer
import ktx.ashley.get
import java.io.Serializable


class TaskInfo(
    val template: TaskTemplate,
    val performer: Entity,   //todo multiple ?
    val target: TaskTarget,
    var started: Boolean = false
): Serializable {

}

class TaskTemplate (
    val performerFilter: TaskPerformerFilter,
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

val moveTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    oneTimeAction = ::moveTaskAction,
    successCondition = ::isNearPositionCondition
)

fun moveTaskAction(e: Entity, t: TaskTarget) {
    t as TaskTarget.PositionTarget  //todo
    send(Event.EntitySetMoveTarget(e, t.position))
}

fun isNearPositionCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.PositionTarget  //todo
    return e[move]!!.path.finished  //todo if the path still match the target?
}