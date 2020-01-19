package com.ovle.rll3.model.util

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.tile.*

fun gridToTileArray(grid: Grid): TileArray {
    val size = grid.width
    val floatValues = grid.array
    val tiles = floatValueArrayToTiles(floatValues, size)
    return TileArray(tiles, size)
}

fun floatValueArrayToTiles(floatValues: FloatArray, size: Int): Array<Tile> {
    return floatValues.mapIndexed { index, value ->
        Tile(
//            point(index % size, index / size),
            gridValueToTileId(value)
        )
    }.toTypedArray()
}

private fun gridValueToTileId(gridValue: Float): Int {
    return when {
        gridValue >= GridFactory.wallTreshold -> wallTileId
        gridValue == GridFactory.floorTreshold -> roomFloorTileId
        gridValue == GridFactory.corridorTreshold -> corridorFloorTileId
        else -> -1
    }
}

fun entityTilePassMapper(tile: Tile) = when(tile.typeId) {
    wallTileId -> TilePassType.Solid
    pitFloorTileId -> TilePassType.Restricted
    else -> TilePassType.Passable
}

fun lightTilePassMapperTrue(tile: Tile) = LightPassType.Passable

fun lightTilePassMapper(tile: Tile) = when(tile.typeId) {
    wallTileId -> LightPassType.Solid
    else -> LightPassType.Passable
}