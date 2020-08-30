package com.ovle.rll3.model.procedural.config.world

import com.ovle.rll3.Tile
import com.ovle.rll3.model.procedural.config.location.outdoorHighWallTreshold
import com.ovle.rll3.model.procedural.config.location.outdoorLowGroundTreshold
import com.ovle.rll3.model.procedural.config.location.outdoorLowWallTreshold
import com.ovle.rll3.model.procedural.config.location.shallowWaterTreshold
import kotlin.random.Random

const val highWorldMountainTreshold = outdoorHighWallTreshold
const val lowWorldMountainTreshold = outdoorLowWallTreshold
const val worldShallowWaterTreshold = outdoorLowGroundTreshold
const val worldDeepWaterTreshold = shallowWaterTreshold

const val heatUpperTreshold = 0.8f
const val heatLowerTreshold = 0.2f


fun tileMapper(heightValue: Float, heatValue: Float): Tile {
    return when {
        heightValue >= highWorldMountainTreshold -> highMountainTileId
        heightValue >= lowWorldMountainTreshold && heatValue >= heatUpperTreshold -> lowDesertMountainTileId
        heightValue >= lowWorldMountainTreshold -> lowMountainTileId
        heightValue <= worldDeepWaterTreshold -> deepWaterTileId
        heightValue <= worldShallowWaterTreshold -> shallowWaterTileId
        heatValue <= heatLowerTreshold -> borealTileId
        heatValue >= heatUpperTreshold -> desertTileId
        else -> temperateTileId
    }
}

fun heatGridValueCombinator(value1: Float, value2: Float, random: Random): Float {
    val isFilterValue =  value2 == 0.0f
    val randomTreshold = 0.1

    return when {
        value1 >= heatUpperTreshold ->
            if (isFilterValue) heatUpperTreshold - 0.1f
            else value1 - random.nextDouble(randomTreshold).toFloat()
        value1 <= heatLowerTreshold ->
            if (isFilterValue) value1 + random.nextDouble(randomTreshold / 2).toFloat()
            else value1 + random.nextDouble(2 * randomTreshold).toFloat()
        else -> value1
    }
}