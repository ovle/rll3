package com.ovle.rll3.model.procedural.config

import com.ovle.rll3.TileType
import com.ovle.rll3.model.tile.*

val outdoorHighWallTreshold = 0.75
val outdoorLowWallTreshold = 0.65
val outdoorGroundTreshold = 0.4
val shallowWaterTreshold = 0.3

val dungeonWallTreshold = 1.0
val dungeonFloorTreshold = 0.6
val dungeonCorridorTreshold = 0.2

fun tileMapper(value: Float): TileType {
    return when {
        value >= outdoorHighWallTreshold -> naturalHighWallTileId
        value >= outdoorLowWallTreshold -> naturalLowWallTileId
        value >= outdoorGroundTreshold -> groundTileId
        value >= shallowWaterTreshold -> shallowWaterTileId
        else -> deepWaterTileId
    }
}

fun dungeonTileMapper(value: Float): TileType {
    return when {
        value >= dungeonWallTreshold -> structureWallSTileId
        value >= dungeonFloorTreshold -> structureFloorSTileId
        value >= dungeonCorridorTreshold -> roadTileId
        else -> throw RuntimeException("illegal tile value: $value")
    }
}

fun dungeonTileFilter(tile: Tile): Boolean {
    return tile.typeId in groundTypes
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