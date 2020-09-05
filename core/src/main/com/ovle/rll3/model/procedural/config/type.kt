package com.ovle.rll3.model.procedural.config

import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.TileMapper2
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import com.ovle.rll3.model.procedural.grid.WorldProcessor
import com.ovle.rll3.model.util.ConnectionStrategy
import kotlin.random.Random

data class RandomParams(
    val seed: Long
) {
    val kRandom = Random(seed)
    val jRandom = java.util.Random(seed)
}

data class LocationGenerationParams(
    val templateName: String,
    val heightMapFactory: GridFactory,
    val postProcessors: Array<LocationProcessor>,
    val tileMapper: TileMapper2
)

data class WorldGenerationParams(
    val templateName: String,
    val heightMapFactory: GridFactory,
    val heatMapFactory: GridFactory,
    val postProcessors: Array<WorldProcessor>,
    val tileMapper: TileMapper2
)

sealed class GridFactoryParams(
    val size: Int
) {
    class DungeonGridFactoryParams(
        size: Int,
        val roomTypes: Array<RoomType>,
        val maxRoomSize: Int,
        val minRoomSize: Int,
        val tolerance: Int, // Max difference between width and height.
        val windingChance: Float,
        val randomConnectorChance: Float
    ) : GridFactoryParams(size)

    class CelullarAutomataGridFactoryParams(
        size: Int,
        val iterationsAmount: Int = 3,
        val radius: Int = 1,
        val deathLimit: Int = 2,  //more will kill the walls
        val birthLimit: Int = 4,
        val aliveChance: Float = 0.6f,
        val lifeCellMarker: Float = 1.0f,
        val deadCellMarker: Float = 0.0f,
        val isHaveWalls: Boolean = false,
        val connectionStrategy: ConnectionStrategy? = null
    ) : GridFactoryParams(size)

    class FractalGridFactoryParams(
        size: Int,
//        val constantNoiseValue: Float = 0.0f,
        val flexibleNoiseValue: Float = 2.0f,
        val startIteration: Int = 3,
        val shouldRandomizeFinalIteration: Boolean = false,
        val stopIteration: Int = -1,
        val initialBorderValues: Array<FloatArray>? = null,
        val noiseGridFactory: GridFactory? = null
    ) : GridFactoryParams(size)

    class GradientGridFactoryParams(
        size: Int,
        val startValue: Float = 0.0f,
        val endValue: Float = 1.0f,
        var isMirrored: Boolean = true,
        var boundsSharpness: Int = 3,
        var dryValueCoeff: Int = 10,
        val isHorisontal: Boolean = false
    ) : GridFactoryParams(size)
}