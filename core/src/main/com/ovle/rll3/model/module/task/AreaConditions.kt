package com.ovle.rll3.model.module.task

import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.TileConditions.isMinable
import com.ovle.rll3.model.tile.isPassable
import com.ovle.rll3.model.util.Area

object AreaConditions {
    fun isFreeArea(a: Area, l: LocationInfo): Boolean = a.points.all { l.tiles.isPassable(it) }
    fun isMinableArea(a: Area, l: LocationInfo): Boolean = a.points.any { isMinable(it, l) }
}