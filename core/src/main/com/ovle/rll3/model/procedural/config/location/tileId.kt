package com.ovle.rll3.model.procedural.config.location

const val whateverTileId = '`'

const val highGroundTileId = '_'
const val lowGroundTileId = 'u'
const val aridTileId = 'a'
const val desertTileId = 's'
const val tundraTileId = 't'
const val jungleTileId = 'j'

const val deepWaterTileId = 'W'
const val shallowWaterTileId = 'w'
const val naturalHighWallTileId = '1'
const val naturalLowWallTileId = '^'

const val corridorTileId = 'c'
const val pitFloorTileId = 'V'
const val structureFloorWTileId = '.'
const val structureFloorSTileId = ':'
const val structureWallWTileId = '#'
const val structureWallSTileId = '='
const val structurePitTileId = 'O'
const val roadTileId = 'r'
const val fenceTileId = 'f'

const val outOfMapTileId = naturalHighWallTileId

val pitTypes = setOf(pitFloorTileId, deepWaterTileId, shallowWaterTileId, structurePitTileId)
val solidWallTypes = setOf(naturalHighWallTileId, naturalLowWallTileId, structureWallWTileId, structureWallSTileId)
val wallTypes = setOf(naturalHighWallTileId, naturalLowWallTileId, structureWallWTileId, structureWallSTileId, fenceTileId)
val floorTypes = setOf(highGroundTileId, lowGroundTileId, desertTileId, tundraTileId, jungleTileId, pitFloorTileId, structureFloorWTileId, structureFloorSTileId)
val groundTypes = setOf(highGroundTileId, lowGroundTileId, desertTileId, tundraTileId, jungleTileId)
val naturalWallTypes = setOf(naturalLowWallTileId, naturalHighWallTileId)
//val structureTypes = setOf(structureFloorWTileId, structureFloorSTileId, structureWallWTileId)
