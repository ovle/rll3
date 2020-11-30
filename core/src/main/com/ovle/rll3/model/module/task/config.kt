package com.ovle.rll3.model.module.task

import com.ovle.rll3.model.module.core.entity.anyTaskPerformer
import com.ovle.rll3.model.module.task.AreaConditions.isFreeArea
import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
import com.ovle.rll3.model.module.task.EntityConditions.isResourceEntity
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
    targetFilter = { t, l -> isPositionTarget(t) && isSource(t.asPositionTarget().position, l) },  //todo not only source
    btName = "mine"
)

val gatherTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, _ -> isEntityTarget(t) && isSourceEntity(t.asEntityTarget().entity) },
    btName = "gather"
)

val attackTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, _ -> isEntityTarget(t) && isLivingEntity(t.asEntityTarget().entity)},   //todo only hostile/food source
    btName = "attack"
)

val carryTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, _ -> isEntityTarget(t) && isResourceEntity(t.asEntityTarget().entity)},    //todo not carried yet
    btName = "carry"
)

val buildTaskTemplate = TaskTemplate(
    performerFilter = ::anyTaskPerformer,
    targetFilter = { t, l -> isAreaTarget(t) && isFreeArea(t.asAreaTarget().area, l) },
    targetMap = { t -> t.asAreaTarget().area.map { TaskTarget.PositionTarget(it) } },   //todo priority from center to border
    btName = "build"
)

//todo
fun isPositionTarget(t: TaskTarget): Boolean = t is TaskTarget.PositionTarget
fun TaskTarget.asPositionTarget() = (this as TaskTarget.PositionTarget)
fun isEntityTarget(t: TaskTarget): Boolean = t is TaskTarget.EntityTarget
fun TaskTarget.asEntityTarget() = (this as TaskTarget.EntityTarget)
fun isAreaTarget(t: TaskTarget): Boolean = t is TaskTarget.AreaTarget
fun TaskTarget.asAreaTarget() = (this as TaskTarget.AreaTarget)

//todo gather without attack (i.e. cow -> milk)
//todo mine non-source tiles
//todo priority by order is not flexible
fun taskTemplates() = arrayOf(
    attackTaskTemplate,
    gatherTaskTemplate,
    mineTaskTemplate,
    carryTaskTemplate,
    buildTaskTemplate
//    moveToTaskTemplate
)