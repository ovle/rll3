package com.ovle.rll3.model.procedural.config

import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.model.procedural.grid.factory.GridFactory
import com.ovle.rll3.model.procedural.grid.processor.TilesProcessor
import com.ovle.rll3.model.procedural.grid.utils.ConnectionStrategy
import com.ovle.rll3.model.tile.TileType
import com.ovle.rll3.view.layer.level.TileTextureInfo
import com.ovle.rll3.view.layer.level.TileToTextureParams


data class LevelParams(
    val templateName: String,
    //main
    val factoryParams: LevelFactoryParams,
    val gridFactory: GridFactory,
    val postProcessors: Array<TilesProcessor>
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
    ): LevelFactoryParams(size)

    class CelullarAutomataLevelFactoryParams(
        size: Int,
        val connectionStrategy: ConnectionStrategy
    ): LevelFactoryParams(size)

    class NoiseLevelFactoryParams(
        size: Int,
        val radius: Int,
        val modifier: Float
    ): LevelFactoryParams(size)

    class TemplateLevelFactoryParams(
        size: Int,
        val template: Array<Array<Int>>
    ): LevelFactoryParams(size)
}