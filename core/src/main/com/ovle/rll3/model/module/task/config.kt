package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.ai.behavior.config.bt.*
import com.ovle.rll3.model.module.task.AreaConditions.isFreeArea
import com.ovle.rll3.model.module.task.AreaConditions.isMinableArea
import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity
import com.ovle.rll3.model.module.task.TileConditions.isMinable
import com.ovle.rlUtil.gdx.math.Area


//val testTaskTemplate = TaskTemplate(
//    performerFilter = ::defaultPerformerFilter,
//    targetFilter = { t, _ -> t.target is Entity },
//    btTemplate = testEntityValidationBt
//)

val gatherTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter,
    targetFilter = { t, _ -> t.target is Entity && isSourceEntity(t.asEntity()) },
    btTemplate = gatherBt
)

val buildTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter,
    targetFilter = { t, l -> (t.target is Area) && isFreeArea(t.target, l) },
    targetMap = { t, _ -> (t.target as Area).points.map { TaskTarget(it) } },   //todo priority from center to border
    btTemplate = buildBt
)

val attackTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter, //todo armed performer
    targetFilter = { t, _ -> t.target is Entity && isLivingEntity(t.target) },   //todo only hostile/food source
    btTemplate = attackBt
)

val mineTaskTemplate = TaskTemplate(
    performerFilter = ::defaultPerformerFilter,
    targetFilter = { t, l -> t.target is Area && isMinableArea(t.target, l) },
    targetMap = { t, l -> (t.target as Area).points.filter { isMinable(it, l) }.map { TaskTarget(it) } },          //todo priority from center to border
    btTemplate = mineBt
)


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
//    testTaskTemplate
//    attackTaskTemplate,
    gatherTaskTemplate,
    buildTaskTemplate,
    mineTaskTemplate
//    moveToTaskTemplate
)