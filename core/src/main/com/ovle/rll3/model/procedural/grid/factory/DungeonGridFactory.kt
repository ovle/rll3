package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import com.github.czyzby.noise4j.map.generator.util.Generators
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.DungeonLevelFactoryParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.GridFactory

class DungeonGridFactory(val params: DungeonLevelFactoryParams): GridFactory {

    companion object {
        const val wallTreshold = 1.0f
        const val floorTreshold = 0.6f
        const val corridorTreshold = 0.2f
    }

    override fun get(random: RandomParams): Grid {
        val grid = Grid(params.size)
        val generator = DungeonGenerator.getInstance()
        Generators.setRandom(random.jRandom)

        generator.apply {
            params.roomTypes.forEach { addRoomType(it) }

            roomGenerationAttempts = 100
            maxRoomSize = params.maxRoomSize
            minRoomSize = params.minRoomSize
            tolerance = params.tolerance

            wallThreshold = wallTreshold
            floorThreshold = floorTreshold
            corridorThreshold = corridorTreshold

            windingChance = params.windingChance
            randomConnectorChance = params.randomConnectorChance
        }.generate(grid)

        return grid
    }
}