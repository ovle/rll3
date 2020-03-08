package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import com.ovle.rll3.model.procedural.config.LevelFactoryParams
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.DungeonLevelFactoryParams

class DungeonGridFactory: GridFactory {

    companion object {
        const val wallTreshold = 1.0f
        const val floorTreshold = 0.6f
        const val corridorTreshold = 0.2f
    }

    override fun get(factoryParams: LevelFactoryParams): Grid {
        factoryParams as DungeonLevelFactoryParams

        val size = factoryParams.size
        val grid = Grid(size)
        val generator = DungeonGenerator.getInstance()

        generator.apply {
            factoryParams.roomTypes.forEach { addRoomType(it) }

            roomGenerationAttempts = 100
            maxRoomSize = factoryParams.maxRoomSize
            minRoomSize = factoryParams.minRoomSize
            tolerance = factoryParams.tolerance

            wallThreshold = wallTreshold
            floorThreshold = floorTreshold
            corridorThreshold = corridorTreshold

            windingChance = factoryParams.windingChance
            randomConnectorChance = factoryParams.randomConnectorChance
        }.generate(grid)

        return grid
    }
}