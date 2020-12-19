package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.adjacentHV
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.entity.freeTaskPerformer
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.util.pathfinding.aStar.path
import ktx.ashley.get

//todo finding path on each update is disaster
fun defaultPerformerFilter(e: Entity, t: TaskTarget, l: LocationInfo) =
    freeTaskPerformer(e) && havePathToAdjPosition(e, t, l)

private fun havePathToAdjPosition(e: Entity, t: TaskTarget, l: LocationInfo): Boolean {
    val from = e[ComponentMappers.position]!!.gridPosition
    val to = t.position()

    return to.adjacentHV().any { path(from, it, l).isNotEmpty() }
}

fun isPositionTarget(t: TaskTarget): Boolean = t is TaskTarget.PositionTarget
fun TaskTarget.asPositionTarget() = (this as TaskTarget.PositionTarget)
fun isEntityTarget(t: TaskTarget): Boolean = t is TaskTarget.EntityTarget
fun TaskTarget.asEntityTarget() = (this as TaskTarget.EntityTarget)
fun isAreaTarget(t: TaskTarget): Boolean = t is TaskTarget.AreaTarget
fun TaskTarget.asAreaTarget() = (this as TaskTarget.AreaTarget)