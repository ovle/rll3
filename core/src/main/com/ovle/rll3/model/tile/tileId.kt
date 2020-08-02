package com.ovle.rll3.model.tile

const val whateverTileId = '`'
const val groundTileId = '_'
const val wallTileId = '1'
const val corridorTileId = '2'
const val pitFloorTileId = '3'
const val outOfMapTileId = wallTileId

const val structureFloorTileId = '.'
const val structureInnerFloorTileId = ':'
const val structureWallWTileId = '#'
const val structureWallSTileId = 'w'
const val structurePitTileId = 'O'
const val roadTileId = 'r'
const val fenceTileId = '='
const val waterTileId = '~'

val pitTypes = setOf(pitFloorTileId, waterTileId)
val solidWallTypes = setOf(wallTileId, structureWallWTileId)
val wallTypes = setOf(wallTileId, structureWallWTileId, fenceTileId)
val roomFloorTypes = setOf(groundTileId, pitFloorTileId, structureFloorTileId)
val floorTypes = setOf(groundTileId, pitFloorTileId, corridorTileId, structureFloorTileId, structureInnerFloorTileId, roadTileId)
val structureTypes = setOf(structureFloorTileId, structureInnerFloorTileId, structureWallWTileId)
