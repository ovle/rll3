package com.ovle.rll3.model.util

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.Tile
import com.ovle.rll3.TileArray
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.tile.solidWallTypes


fun gridToTileArray(grid: Grid, mapFunction: (Float) -> Tile): TileArray {
    val size = grid.width
    val floatValues = grid.array
    val tiles = floatValues.map { mapFunction(it) }.toTypedArray()
    return TileArray(tiles, size)
}

fun lightTilePassMapper(tile: Tile) = when(tile) {
    in solidWallTypes -> LightPassType.Solid
    else -> LightPassType.Passable
}
