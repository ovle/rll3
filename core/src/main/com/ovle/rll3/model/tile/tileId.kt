package com.ovle.rll3.model.tile

const val whateverTileId = '_'
const val roomFloorTileId = '0'
const val wallTileId = '1'
const val corridorFloorTileId = '2'
const val pitFloorTileId = '3'
const val outOfMapTileId = wallTileId

const val structureFloorTileId = '.'
const val structureInnerFloorTileId = ':'
const val structureWallTileId = '#'
const val structurePitTileId = 'O'
const val roadTileId = 'r'
const val fenceTileId = '='
const val waterTileId = '~'

val pitTypes = setOf(pitFloorTileId, waterTileId)
val solidWallTypes = setOf(wallTileId, structureWallTileId)
val wallTypes = setOf(wallTileId, structureWallTileId, fenceTileId)
val roomFloorTypes = setOf(roomFloorTileId, pitFloorTileId, structureFloorTileId)
val floorTypes = setOf(roomFloorTileId, pitFloorTileId, corridorFloorTileId, structureFloorTileId, structureInnerFloorTileId, roadTileId)
val structureTypes = setOf(structureFloorTileId, structureInnerFloorTileId, structureWallTileId)
