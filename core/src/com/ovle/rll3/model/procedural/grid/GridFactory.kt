package com.ovle.rll3.model.procedural.grid

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.AbstractRoomGenerator
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.corridorTreshold
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.floorTreshold
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.wallTreshold
import com.ovle.rll3.model.procedural.grid.ext.ExtDungeonGenerator

interface GridFactory {
    companion object {
        const val wallTreshold = 1.0f
        const val floorTreshold = 0.6f
        const val corridorTreshold = 0.2f
    }

    fun get(size: Int, settings: DungeonGenerationSettings): GridWrapper
}

class DungeonGenerationSettings(
    val roomTypes: Array<RoomType>,
    val maxRoomSize: Int,
    val minRoomSize: Int,
    val tolerance: Int, // Max difference between width and height.
    val windingChance: Float,
    val randomConnectorChance: Float
)

data class GridWrapper(val grid: Grid, val rooms: List<AbstractRoomGenerator.Room>)

class DungeonGridFactory: GridFactory {

    override fun get(size: Int, settings: DungeonGenerationSettings): GridWrapper {
        val grid = Grid(size)
        val dungeonGenerator = ExtDungeonGenerator()

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

        return GridWrapper(grid, dungeonGenerator.rooms())
    }
}