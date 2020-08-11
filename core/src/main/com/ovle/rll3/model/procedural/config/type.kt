package com.ovle.rll3.model.procedural.config

import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.TileTypeMapper
import com.ovle.rll3.model.procedural.grid.factory.GridFactory
import com.ovle.rll3.model.procedural.grid.processor.TilesProcessor
import com.ovle.rll3.model.procedural.grid.utils.ConnectionStrategy
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
    val postProcessors: Array<TilesProcessor>,
    val tileMapper: TileTypeMapper
)

sealed class LevelFactoryParams(
    val size: IntRange
) {
    class DungeonLevelFactoryParams(
        size: IntRange,
        val roomTypes: Array<RoomType>,
        val maxRoomSize: Int,
        val minRoomSize: Int,
        val tolerance: Int, // Max difference between width and height.
        val windingChance: Float,
        val randomConnectorChance: Float
    ): LevelFactoryParams(size)

    class CelullarAutomataLevelFactoryParams(
        size: IntRange,
        val connectionStrategy: ConnectionStrategy
    ): LevelFactoryParams(size)

    class NoiseLevelFactoryParams(
        size: IntRange,
        val radius: Int,
        val modifier: Float
    ): LevelFactoryParams(size)

    class TemplateLevelFactoryParams(
        size: IntRange,
        val template: Array<Array<Int>>
    ): LevelFactoryParams(size)

    class FractalLevelFactoryParams(
        size: IntRange
//        val startIteration: Int,
//        val constantNoiseValue: Float,
//        val flexibleNoiseValue: Float
    ): LevelFactoryParams(size)

}