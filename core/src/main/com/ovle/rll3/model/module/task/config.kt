package com.ovle.rll3.model.module.task

import com.ovle.rll3.model.module.core.entity.anyTaskPerformer
import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity
import com.ovle.rll3.model.module.task.TileConditions.isPassable
import com.ovle.rll3.model.module.task.TileConditions.isSource


val moveToTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, l -> isPositionTarget(t) && isPassable(t.asPositionTarget().position, l) },
    btName = "moveTo"
)

val mineTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, l -> isPositionTarget(t) && isSource(t.asPositionTarget().position, l) },
    btName = "mine"
)

val gatherTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, _ -> isEntityTarget(t) && isSourceEntity(t.asEntityTarget().entity) },
    btName = "gather"
)

val attackTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, _ -> isEntityTarget(t) && isLivingEntity(t.asEntityTarget().entity)},
    btName = "attack"
)

//todo
fun isPositionTarget(t: TaskTarget): Boolean = t is TaskTarget.PositionTarget
fun TaskTarget.asPositionTarget() = (this as TaskTarget.PositionTarget)
fun isEntityTarget(t: TaskTarget): Boolean = t is TaskTarget.EntityTarget
fun TaskTarget.asEntityTarget() = (this as TaskTarget.EntityTarget)

fun taskTemplates() = arrayOf(
    gatherTaskTemplate,
    attackTaskTemplate,
    mineTaskTemplate
//    moveToTaskTemplate
)