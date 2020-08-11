package com.ovle.rll3.model.module.task

import com.ovle.rll3.model.module.core.entity.anyTaskPerformer

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

fun taskTemplates() = arrayOf(gatherTaskTemplate, attackTaskTemplate, moveToTaskTemplate)