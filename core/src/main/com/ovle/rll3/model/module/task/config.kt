package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.freeTaskPerformer
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.AreaConditions.isFreeArea
import com.ovle.rll3.model.module.task.AreaConditions.isMinableArea
import com.ovle.rll3.model.module.task.EntityConditions.isLivingEntity
import com.ovle.rll3.model.module.task.EntityConditions.isResourceEntity
import com.ovle.rll3.model.module.task.EntityConditions.isSourceEntity
import com.ovle.rll3.model.module.task.TileConditions.isMinable
import com.ovle.rll3.model.module.task.TileConditions.isPassable
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.adjacentHV
import com.ovle.rll3.model.module.ai.bt.config.testTreeInfo
import ktx.ashley.get


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
    targetFilter = { t, _ -> isEntityTarget(t) && isSourceEntity(t.asEntityTarget().entity) },
    btInfo = testTreeInfo
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