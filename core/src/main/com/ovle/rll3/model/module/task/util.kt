package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.adjacentHV
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.EntityConditions.isTaskPerformer
import com.ovle.rll3.model.util.pathfinding.aStar.path
import ktx.ashley.get

//todo finding path on each update is disaster
fun defaultPerformerFilter(e: Entity, t: TaskTarget, l: LocationInfo) =
    isTaskPerformer(e) && havePathToAdjPosition(e, t, l)

private fun havePathToAdjPosition(e: Entity, t: TaskTarget, l: LocationInfo): Boolean {
    val from = e[position]!!.gridPosition
    val to = t.position()

    return to.adjacentHV().any { path(from, it, l).isNotEmpty() }
}