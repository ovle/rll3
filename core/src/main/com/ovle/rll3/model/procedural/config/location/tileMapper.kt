package com.ovle.rll3.model.procedural.config.location

import com.ovle.rll3.Tile
import com.ovle.rll3.model.procedural.config.world.heatBorealTreshold
import com.ovle.rll3.model.procedural.config.world.heatDesertTreshold

//const val dungeonWallTreshold = 1.0f
//const val dungeonFloorTreshold = 0.6f
//const val dungeonCorridorTreshold = 0.2f


fun dungeonTileMapper(value: Float): Tile {
    return floorTileId
//    return when {
//        value >= dungeonWallTreshold -> structureWallSTileId
//        value >= dungeonFloorTreshold -> structureFloorSTileId
//        value >= dungeonCorridorTreshold -> roadTileId
//        else -> throw RuntimeException("illegal tile value: $value")
//    }
}