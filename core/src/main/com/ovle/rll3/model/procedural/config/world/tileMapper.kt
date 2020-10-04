package com.ovle.rll3.model.procedural.config.world

import com.ovle.rll3.Tile

const val outdoorHighWallTreshold = 0.85f
const val outdoorLowWallTreshold = 0.7f
const val outdoorHighGroundTreshold = 0.6f
const val outdoorLowGroundTreshold = 0.5f
const val shallowWaterTreshold = 0.425f

const val highWorldMountainTreshold = outdoorHighWallTreshold
const val lowWorldMountainTreshold = outdoorLowWallTreshold
const val worldShallowWaterTreshold = outdoorLowGroundTreshold
const val worldDeepWaterTreshold = shallowWaterTreshold

const val heatDesertTreshold = 0.85f
const val heatAridTreshold = 0.75f
const val heatBorealTreshold = 0.35f
const val heatArcticTreshold = 0.15f


fun tileMapper(heightValue: Float, heatValue: Float): Tile {
    return when {
        heightValue >= highWorldMountainTreshold -> highMountainTileId
        heightValue >= lowWorldMountainTreshold -> lowMountainTileId
        heightValue <= worldDeepWaterTreshold -> deepWaterTileId
        heightValue <= worldShallowWaterTreshold -> shallowWaterTileId
        heatValue <= heatArcticTreshold -> arcticTileId
        heatValue <= heatBorealTreshold -> borealTileId
        heatValue >= heatDesertTreshold -> desertTileId
        heatValue >= heatAridTreshold -> aridTileId
        else -> temperateTileId
    }
}