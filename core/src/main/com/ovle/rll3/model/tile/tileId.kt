package com.ovle.rll3.model.tile

const val whateverTileId = '`'
const val groundTileId = '_'
const val naturalHighWallTileId = '1'
const val naturalLowWallTileId = '^'
const val corridorTileId = 'c'
const val pitFloorTileId = 'V'
const val outOfMapTileId = naturalHighWallTileId

const val structureFloorWTileId = '.'
const val structureFloorSTileId = ':'
const val structureWallWTileId = '#'
const val structureWallSTileId = '='
const val structurePitTileId = 'O'
const val roadTileId = 'r'
const val fenceTileId = 'f'
const val deepWaterTileId = '~'
const val shallowWaterTileId = '"'


val pitTypes = setOf(pitFloorTileId, deepWaterTileId, shallowWaterTileId, structurePitTileId)
val solidWallTypes = setOf(naturalHighWallTileId, naturalLowWallTileId, structureWallWTileId, structureWallSTileId)
val wallTypes = setOf(naturalHighWallTileId, naturalLowWallTileId, structureWallWTileId, structureWallSTileId, fenceTileId)
val floorTypes = setOf(groundTileId, pitFloorTileId, structureFloorWTileId, structureFloorSTileId)
val groundTypes = setOf(groundTileId)
val naturalWallTypes = setOf(naturalLowWallTileId, naturalHighWallTileId)
//val structureTypes = setOf(structureFloorWTileId, structureFloorSTileId, structureWallWTileId)
