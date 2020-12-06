package com.ovle.rll3.model.module.task

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.TileConditions.isMinable
import com.ovle.rll3.model.tile.isPassable

object AreaConditions {
    fun isFreeArea(a: Collection<GridPoint2>, l: LocationInfo): Boolean = a.all { l.tiles.isPassable(it) }
    fun isMinableArea(a: Collection<GridPoint2>, l: LocationInfo): Boolean = a.any { isMinable(it, l) }
}