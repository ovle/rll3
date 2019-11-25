package com.ovle.rll3.model.procedural.grid

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.corridorTreshold
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.floorTreshold
import com.ovle.rll3.model.procedural.grid.GridFactory.Companion.wallTreshold

interface GridFactory {
    companion object {
        const val wallTreshold = 1.0f
        const val floorTreshold = 0.6f
        const val corridorTreshold = 0.2f
    }

    fun get(size: Int): Grid
}

class DungeonGridFactory: GridFactory {

    override fun get(size: Int): Grid {
        val grid = Grid(size)

        with(DungeonGenerator.getInstance()) {
            addRoomType(RoomType.DefaultRoomType.SQUARE)
            addRoomType(RoomType.DefaultRoomType.ROUNDED)
            addRoomType(RoomType.DefaultRoomType.CASTLE)
        }

        DungeonGenerator.getInstance().apply {
            roomGenerationAttempts = 100
            maxRoomSize = 15
            minRoomSize = 3
            tolerance = 5 // Max difference between width and height.

            wallThreshold = wallTreshold
            floorThreshold = floorTreshold
            corridorThreshold = corridorTreshold

            windingChance = 0.25f
            randomConnectorChance = 0.05f
        }.generate(grid)

        return grid
    }
}