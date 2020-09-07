package com.ovle.rll3.model.procedural.config.location

import com.ovle.rll3.Tile
import com.ovle.rll3.model.procedural.config.world.heatBorealTreshold
import com.ovle.rll3.model.procedural.config.world.heatDesertTreshold

const val outdoorHighWallTreshold = 0.85f
const val outdoorLowWallTreshold = 0.7f
const val outdoorHighGroundTreshold = 0.6f
const val outdoorLowGroundTreshold = 0.5f
const val shallowWaterTreshold = 0.425f

const val dungeonWallTreshold = 1.0f
const val dungeonFloorTreshold = 0.6f
const val dungeonCorridorTreshold = 0.2f


fun tileMapper(heightValue: Float, heatValue: Float): Tile {
    return when {
        heightValue >= outdoorHighWallTreshold -> naturalHighWallTileId
        heightValue >= outdoorLowWallTreshold -> naturalLowWallTileId
        heightValue <= outdoorLowGroundTreshold -> shallowWaterTileId
        heightValue <= shallowWaterTreshold -> deepWaterTileId

        heatValue <= heatBorealTreshold -> tundraTileId
        heatValue >= heatDesertTreshold -> desertTileId
        heightValue >= outdoorHighGroundTreshold -> highGroundTileId
        heightValue >= outdoorLowGroundTreshold -> lowGroundTileId

        else -> throw RuntimeException("illegal tile value: $heightValue/$heatValue")
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