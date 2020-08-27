package com.ovle.rll3.model.procedural.config

import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.TileTypeMapper
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.procedural.grid.LevelProcessor
import com.ovle.rll3.model.procedural.grid.util.ConnectionStrategy
import kotlin.random.Random

data class RandomParams(
    val seed: Long
) {
    val kRandom = Random(seed)
    val jRandom = java.util.Random(seed)
}

data class LevelParams(
    val templateName: String,
    val factory: GridFactory,
    val postProcessors: Array<LevelProcessor>,
    val tileMapper: TileTypeMapper
)

sealed class LevelFactoryParams(
    val size: Int
) {
    class DungeonLevelFactoryParams(
        size: Int,
        val roomTypes: Array<RoomType>,
        val maxRoomSize: Int,
        val minRoomSize: Int,
        val tolerance: Int, // Max difference between width and height.
        val windingChance: Float,
        val randomConnectorChance: Float
    ) : LevelFactoryParams(size)

    class CelullarAutomataLevelFactoryParams(
        size: Int,
        val connectionStrategy: ConnectionStrategy
    ) : LevelFactoryParams(size)

    class NoiseLevelFactoryParams(
        size: Int,
        val radius: Int,
        val modifier: Float
    ) : LevelFactoryParams(size)

    class TemplateLevelFactoryParams(
        size: Int,
        val template: Array<Array<Int>>
    ) : LevelFactoryParams(size)

    class FractalLevelFactoryParams(
        size: Int,
        var constantNoiseValue: Float = 0.0f,
        var flexibleNoiseValue: Float = 2.0f,
        var startIteration: Int = 3,
        var shouldRandomizeFinalIteration: Boolean = false,
        var stopIteration: Int = -1,
        var initialBorderValues: Array<FloatArray>? = null
    ) : LevelFactoryParams(size)

    class GradientLevelFactoryParams(
        size: Int,
        val startValue: Float = 0.0f,
        val endValue: Float = 1.0f,
        var isMirrored: Boolean = true,
        var boundsSharpness: Int = 3,
        var dryValueCoeff: Int = 10,
        val isHorisontal: Boolean = false
    ) : LevelFactoryParams(size)
}