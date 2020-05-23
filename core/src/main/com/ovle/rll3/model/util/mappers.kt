package com.ovle.rll3.model.util

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.grid.factory.CelullarAutomataGridFactory
import com.ovle.rll3.model.procedural.grid.factory.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.factory.NoiseGridFactory
import com.ovle.rll3.model.tile.*


fun gridToTileArray(grid: Grid, mapFunction: (Float) -> TileType): TileArray {
    val size = grid.width
    val floatValues = grid.array
    val tiles = floatValues.map {
        Tile(mapFunction(it))
    }.toTypedArray()
    return TileArray(tiles, size)
}

fun dungeonGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= DungeonGridFactory.wallTreshold -> wallTileId
        gridValue == DungeonGridFactory.floorTreshold -> roomFloorTileId
        gridValue == DungeonGridFactory.corridorTreshold -> corridorFloorTileId
        else -> throw RuntimeException("illegal tile value: $gridValue")
    }
}

fun caveGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= CelullarAutomataGridFactory.wallMarker -> wallTileId
        gridValue >= CelullarAutomataGridFactory.pitMarker -> pitFloorTileId
        else -> roomFloorTileId //todo
    }
}

fun villageGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= NoiseGridFactory.wallTreshold -> wallTileId
        gridValue >= NoiseGridFactory.floorTreshold -> roomFloorTileId
        else -> waterTileId //todo
    }
}

fun lightTilePassMapperTrue(tile: Tile) = LightPassType.Passable

fun lightTilePassMapper(tile: Tile) = when(tile.typeId) {
    in solidWallTypes -> LightPassType.Solid
    else -> LightPassType.Passable
}
