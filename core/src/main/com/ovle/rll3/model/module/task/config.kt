package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity
import com.ovle.rll3.model.module.ai.bt.config.gather


//val moveToTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, l -> isPositionTarget(t) && isPassable(t.asPositionTarget().position, l) },
//    btName = "moveTo"
//)
//
//val mineTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, l -> isAreaTarget(t) && isMinableArea(t.asAreaTarget().area, l) },     //todo not only source
//    targetMap = { t, l -> t.asAreaTarget().area.filter { isMinable(it, l) }.map { TaskTarget.PositionTarget(it) } },          //todo priority from center to border
//    btName = "mine"
//)

val gatherTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter,
    targetFilter = { t, _ -> t.target is Entity && isSourceEntity(t.asEntity()) },
    btInfo = gather
)

//val attackTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, _ -> isEntityTarget(t) && isLivingEntity(t.asEntityTarget().entity)},   //todo only hostile/food source
//    btName = "attack"
//)
//
//val carryTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, _ -> isEntityTarget(t) && isResourceEntity(t.asEntityTarget().entity)},    //todo not carried yet
//    btName = "carry"
//)
//
//val buildTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, l -> isAreaTarget(t) && isFreeArea(t.asAreaTarget().area, l) },
//    targetMap = { t, _ -> t.asAreaTarget().area.map { TaskTarget.PositionTarget(it) } },   //todo priority from center to border
//    btName = "build"
//)
//
//val fillWithWaterTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, l -> isAreaTarget(t) && isFreeArea(t.asAreaTarget().area, l) },
//    btName = "fillWithWater"
//)



//todo gather without attack (i.e. cow -> milk)
//todo mine non-source tiles
//todo priority by order is not flexible
fun taskTemplates() = arrayOf(
//    attackTaskTemplate,
    gatherTaskTemplate
//    carryTaskTemplate,
//
//    mineTaskTemplate,
//    buildTaskTemplate
//    moveToTaskTemplate
)