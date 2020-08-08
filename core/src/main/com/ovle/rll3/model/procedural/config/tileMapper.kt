package com.ovle.rll3.model.procedural.config

import com.ovle.rll3.TileType
import com.ovle.rll3.model.tile.groundTileId
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.model.tile.waterTileId

fun tileMapper(value: Float): TileType {
    return when {
        value >= 0.7 -> wallTileId
        value >= 0.4 -> groundTileId
        else -> waterTileId
    }
}

/*
fun dungeonGridValueToTileType(gridValue: Float): TileType {
return when {
    gridValue >= DungeonGridFactory.wallTreshold -> wallTileId
    gridValue == DungeonGridFactory.floorTreshold -> groundTileId
    gridValue == DungeonGridFactory.corridorTreshold -> corridorTileId
    else -> throw RuntimeException("illegal tile value: $gridValue")
}
}

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