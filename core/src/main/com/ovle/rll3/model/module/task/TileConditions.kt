package com.ovle.rll3.model.module.task

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.config.location.minableTypes
import com.ovle.rll3.model.tile.isPassable

object TileConditions {
    fun isSource(p: GridPoint2, l: LocationInfo) = l.tiles[p.x, p.y] in minableTypes

    fun isPassable(p: GridPoint2, l: LocationInfo) = l.tiles.isPassable(p)
}