package com.ovle.rll3.model.util

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.*
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.procedural.config.location.wallTileId

fun gridToTileArray(grid: Grid, mapFunction: (Float) -> Tile): TileArray {
    val size = grid.width
    val floatValues = grid.array
    val tiles = floatValues.map { mapFunction(it) }.toTypedArray()
    return TileArray(tiles, size)
}

fun gridToTileArray(grid1: Grid, grid2: Grid, mapFunction: TileMapper2): TileArray {
    check(grid1.width == grid2.width)
    check(grid1.height == grid2.height)

    val size = grid1.width
    val floatValues = grid1.array.zip(grid2.array)
    val tiles = floatValues.map { mapFunction(it.first, it.second) }.toTypedArray()
    return TileArray(tiles, size)
}

fun lightTilePassMapper(tile: Tile) = when(tile) {
    wallTileId -> LightPassType.Solid
    else -> LightPassType.Passable
}
