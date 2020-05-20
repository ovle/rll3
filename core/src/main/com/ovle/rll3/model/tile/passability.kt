package com.ovle.rll3.model.tile

import com.badlogic.gdx.math.GridPoint2


fun tilePassType(tile: Tile) = when(tile.typeId) {
    in wallTypes -> TilePassType.Solid
    in pitTypes -> TilePassType.Restricted
    else -> TilePassType.Passable
}

fun TileArray.isPassable(point: GridPoint2) = tilePassType(this.tile(point.x, point.y)) == TilePassType.Passable