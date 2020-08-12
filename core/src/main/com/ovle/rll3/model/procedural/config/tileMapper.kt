package com.ovle.rll3.model.procedural.config

import com.ovle.rll3.Tile
import com.ovle.rll3.model.tile.*

val outdoorHighWallTreshold = 0.8
val outdoorLowWallTreshold = 0.7
val outdoorHighGroundTreshold = 0.5
val outdoorLowGroundTreshold = 0.4
val shallowWaterTreshold = 0.3

val dungeonWallTreshold = 1.0
val dungeonFloorTreshold = 0.6
val dungeonCorridorTreshold = 0.2

fun tileMapper(value: Float): Tile {
    return when {
        value >= outdoorHighWallTreshold -> naturalHighWallTileId
        value >= outdoorLowWallTreshold -> naturalLowWallTileId
        value >= outdoorHighGroundTreshold -> highGroundTileId
        value >= outdoorLowGroundTreshold -> lowGroundTileId
        value >= shallowWaterTreshold -> shallowWaterTileId
        else -> deepWaterTileId
    }
}

fun dungeonTileMapper(value: Float): Tile {
    return when {
        value >= dungeonWallTreshold -> structureWallSTileId
        value >= dungeonFloorTreshold -> structureFloorSTileId
        value >= dungeonCorridorTreshold -> roadTileId
        else -> throw RuntimeException("illegal tile value: $value")
    }
}

fun groundTileFilter(tile: Tile): Boolean {
    return tile in groundTypes
}

/*
fun caveGridValueToTileType(gridValue: Float): TileType {
return when {
    gridValue >= CelullarAutomataGridFactory.wallMarker -> wallTileId
    gridValue >= CelullarAutomataGridFactory.pitMarker -> pitFloorTileId
    else -> groundTileId //todo
}
}

fun villageGridValueToTileType(gridValue: Float): TileType {
return when {
    gridValue >= NoiseGridFactory.wallTreshold -> wallTileId
    gridValue >= NoiseGridFactory.floorTreshold -> groundTileId
    else -> waterTileId //todo
}
}
*/