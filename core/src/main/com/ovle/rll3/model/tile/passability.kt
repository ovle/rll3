package com.ovle.rll3.model.tile

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.Tile
import com.ovle.rlUtil.TileArray
import com.ovle.rll3.model.procedural.config.location.pitTypes
import com.ovle.rll3.model.procedural.config.location.wallTypes


fun tilePassType(tile: Tile) = when(tile) {
    in wallTypes -> false //TilePassType.Solid
    in pitTypes -> false //TilePassType.Restricted
    else -> true //TilePassType.Passable
}

fun TileArray.isPassable(point: GridPoint2) = tilePassType(this[point.x, point.y]) == true //TilePassType.Passable