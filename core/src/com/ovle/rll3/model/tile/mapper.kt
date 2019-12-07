package com.ovle.rll3.model.tile

import com.badlogic.gdx.math.Vector2

fun vectorCoords(tile: Tile) = Vector2(
    tile.x.toFloat(),
    tile.y.toFloat()
)

fun entityTilePassMapper(tile: Tile) = when(tile.typeId) {
    wallTileId -> TilePassType.Solid
    pitFloorTileId -> TilePassType.Restricted
    else -> TilePassType.Passable
}

fun lightTilePassMapper(tile: Tile) = when(tile.typeId) {
    wallTileId -> TilePassType.Solid
    else -> TilePassType.Passable
}