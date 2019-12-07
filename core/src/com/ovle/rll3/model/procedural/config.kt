package com.ovle.rll3.model.procedural

import com.ovle.rll3.model.tile.corridorFloorTileId
import com.ovle.rll3.model.tile.pitFloorTileId
import com.ovle.rll3.model.tile.roomFloorTileId

const val mapSizeInTiles = 33
const val lightSourceChance = 0.25f

val roomFloorTypes = setOf(roomFloorTileId, pitFloorTileId)
val corridorFloorTypes = setOf(corridorFloorTileId)
val floorTypes = setOf(roomFloorTileId, pitFloorTileId, corridorFloorTileId)