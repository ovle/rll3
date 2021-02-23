package com.ovle.rll3.model.util.conditions

import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.util.conditions.TileConditions.isMinable
import com.ovle.rll3.model.tile.isPassable
import com.ovle.rlUtil.gdx.math.Area

object AreaConditions {
    fun isFreeArea(a: Area, l: LocationInfo): Boolean = a.points.all { l.tiles.isPassable(it) }
    fun isMinableArea(a: Area, l: LocationInfo): Boolean = a.points.any { isMinable(it, l) }
}