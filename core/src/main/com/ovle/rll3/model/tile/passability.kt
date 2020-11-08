package com.ovle.rll3.model.tile

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Tile
import com.ovle.rll3.TileArray
import com.ovle.rll3.model.procedural.config.location.pitTileId
import com.ovle.rll3.model.procedural.config.location.wallTileId


fun tilePassType(tile: Tile) = when(tile) {
    wallTileId -> TilePassType.Solid
    pitTileId -> TilePassType.Restricted
    else -> TilePassType.Passable
}

fun TileArray.isPassable(point: GridPoint2) = this.isPointValid(point)
    && tilePassType(this[point.x, point.y]) == TilePassType.Passable