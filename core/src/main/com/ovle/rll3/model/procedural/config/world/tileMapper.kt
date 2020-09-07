package com.ovle.rll3.model.procedural.config.world

import com.ovle.rll3.Tile
import com.ovle.rll3.model.procedural.config.location.outdoorHighWallTreshold
import com.ovle.rll3.model.procedural.config.location.outdoorLowGroundTreshold
import com.ovle.rll3.model.procedural.config.location.outdoorLowWallTreshold
import com.ovle.rll3.model.procedural.config.location.shallowWaterTreshold

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