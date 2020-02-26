package com.ovle.rll3.model.procedural.grid

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.DungeonGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.TemplateGenerationSettings
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.corridorTreshold
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.floorTreshold
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.wallTreshold

interface GridFactory {
    companion object {
        const val wallTreshold = 1.0f
        const val floorTreshold = 0.6f
        const val corridorTreshold = 0.2f
    }

    fun get(size: Int, settings: LevelGenerationSettings): Grid
}

class DungeonGridFactory: GridFactory {

    override fun get(size: Int, settings: LevelGenerationSettings): Grid {
        settings as DungeonGenerationSettings

        val grid = Grid(size)
        val dungeonGenerator = DungeonGenerator.getInstance()

        dungeonGenerator.apply {
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

//todo
class TemplateGridFactory: GridFactory {
    override fun get(size: Int, settings: LevelGenerationSettings): Grid {
        settings as TemplateGenerationSettings

        val grid = Grid(size)
        return grid
    }

    //private fun tileIndexToTile(index: Int, size: Int) = Tile(typeId = template[index % size][index / size])
}