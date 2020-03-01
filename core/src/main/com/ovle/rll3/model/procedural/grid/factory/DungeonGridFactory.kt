package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.DungeonGenerationSettings

class DungeonGridFactory: GridFactory {

    companion object {
        const val wallTreshold = 1.0f
        const val floorTreshold = 0.6f
        const val corridorTreshold = 0.2f
    }

    override fun get(size: Int, settings: LevelGenerationSettings): Grid {
        settings as DungeonGenerationSettings

        val grid = Grid(size)
        val generator = DungeonGenerator.getInstance()

        generator.apply {
            settings.roomTypes.forEach { addRoomType(it) }

            roomGenerationAttempts = 100
            maxRoomSize = settings.maxRoomSize
            minRoomSize = settings.minRoomSize
            tolerance = settings.tolerance

            wallThreshold = wallTreshold
            floorThreshold = floorTreshold
            corridorThreshold = corridorTreshold

            windingChance = settings.windingChance
            randomConnectorChance = settings.randomConnectorChance
        }.generate(grid)

        return grid
    }
}