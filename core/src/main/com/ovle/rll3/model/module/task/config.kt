package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.Area
import com.ovle.rll3.model.module.ai.behavior.config.bt.attackBt
import com.ovle.rll3.model.module.ai.behavior.config.bt.buildBt
import com.ovle.rll3.model.module.ai.behavior.config.bt.gatherBt
import com.ovle.rll3.model.module.task.AreaConditions.isFreeArea
import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity


val gatherTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter,
    targetFilter = { t, _ -> t.target is Entity && isSourceEntity(t.asEntity()) },
    btTemplate = gatherBt
)

val buildTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter,
    targetFilter = { t, l -> (t.target is Collection<*>) && isFreeArea(t.target as Area, l) },
    targetMap = { t, _ -> (t.target as Area).map { TaskTarget(it) } },   //todo priority from center to border
    btTemplate = buildBt
)

val attackTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter, //todo armed performer
    targetFilter = { t, _ -> t.target is Entity && isLivingEntity(t.target) },   //todo only hostile/food source
    btTemplate = attackBt
)


//val moveToTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, l -> isPositionTarget(t) && isPassable(t.asPositionTarget().position, l) },
//    btName = "moveTo"
//)

//val mineTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, l -> t.target is Area && isMinableArea(t.asAreaTarget().area, l) },     //todo not only source
//    targetMap = { t, l -> t.asAreaTarget().area.filter { isMinable(it, l) }.map { TaskTarget.PositionTarget(it) } },          //todo priority from center to border
//    btName = "mine"
//)


//val testTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, _ -> t.target is Entity },
//    btTemplate = testBt

//)
//
//val fillWithWaterTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, l -> isAreaTarget(t) && isFreeArea(t.asAreaTarget().area, l) },
//    btName = "fillWithWater"
//)


//todo todo check invalid actions(i.e. attack/gather from distance) on mechanics level (not only in bt)

//todo gather without attack (i.e. cow -> milk)
//todo mine non-source tiles
//todo priority by order is not flexible
fun taskTemplates() = arrayOf(
    attackTaskTemplate,
    gatherTaskTemplate,
    buildTaskTemplate
//    testTaskTemplate
//    carryTaskTemplate,
//    mineTaskTemplate,
//    moveToTaskTemplate
)