package com.ovle.rll3.model.procedural

import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.model.procedural.grid.DungeonGenerationSettings
import com.ovle.rll3.model.tile.corridorFloorTileId
import com.ovle.rll3.model.tile.pitFloorTileId
import com.ovle.rll3.model.tile.roomFloorTileId

const val mapSizeInTiles = 55
const val lightSourceChance = 0.25f
const val trapChance = 0.05f

val roomFloorTypes = setOf(roomFloorTileId, pitFloorTileId)
val corridorFloorTypes = setOf(corridorFloorTileId)
val floorTypes = setOf(roomFloorTileId, pitFloorTileId, corridorFloorTileId)

//todo
val dungeonGenerationSettings = DungeonGenerationSettings(
    roomTypes = arrayOf(RoomType.DefaultRoomType.SQUARE, RoomType.DefaultRoomType.ROUNDED, RoomType.DefaultRoomType.CASTLE, RoomType.DefaultRoomType.DIAMOND),
    maxRoomSize = 15,
    minRoomSize = 5,
    tolerance = 5,
    windingChance = 0.25f,
    randomConnectorChance = 0.05f
)
