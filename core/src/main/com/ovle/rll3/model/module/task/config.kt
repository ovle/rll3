package com.ovle.rll3.model.module.task

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.anyTaskPerformer
import com.ovle.rll3.model.module.core.entity.freeTaskPerformer
import com.ovle.rll3.near
import ktx.ashley.get

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

val digTaskTemplate = TaskTemplate(
    performerFilter = ::freeTaskPerformer,
    targetFilter = ::isPositionCondition,
    oneTimeAction = ::moveTaskAction,
    successCondition = ::isAtPositionCondition
)

val attackTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = ::isLivingEntityCondition,
    everyTurnAction = ::attackAction,
    successCondition = ::isTargetDeadCondition
)

val gatherTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = ::isSourceEntityCondition,
    preconditions = listOf(
        TaskPrecondition(
            condition = ::isNearEntityCondition,
            fulfillTask = {
                e, t ->
                //todo
                val gridPosition = (t as TaskTarget.EntityTarget).entity[position]!!.gridPosition.near().random()
                send(Event.GameEvent.EntityStartMoveCommand(e, gridPosition))
//                send(Event.GameEvent.CheckTaskCommand())
            }
        )
    ),
    everyTurnAction = ::gatherAction,
    successCondition = { e, t -> !isTargetExistsCondition(e, t) }
//    failCondition = { e, t -> !isNearEntityCondition(e, t) }
)

fun taskTemplates() = arrayOf(gatherTaskTemplate, attackTaskTemplate, moveToTaskTemplate)