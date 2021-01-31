package com.ovle.rll3.model.module.task

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.Tile
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.config.location.minableTypes
import com.ovle.rll3.model.tile.isPassable

object TileConditions {
    fun isMinable(p: GridPoint2, l: LocationInfo) = isSource(l.tiles[p.x, p.y]) //todo
    fun isSource(p: GridPoint2, l: LocationInfo) = isSource(l.tiles[p.x, p.y])
    fun isSource(t: Tile) = t in minableTypes
    fun isPassable(p: GridPoint2, l: LocationInfo) = l.tiles.isPassable(p)
}