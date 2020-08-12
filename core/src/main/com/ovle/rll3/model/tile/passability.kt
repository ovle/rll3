package com.ovle.rll3.model.tile

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Tile
import com.ovle.rll3.TileArray


fun tilePassType(tile: Tile) = when(tile) {
    in wallTypes -> TilePassType.Solid
    in pitTypes -> TilePassType.Restricted
    else -> TilePassType.Passable
}

fun TileArray.isPassable(point: GridPoint2) = tilePassType(this[point.x, point.y]) == TilePassType.Passable