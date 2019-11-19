package com.ovle.rll3.model.procedural

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import com.ovle.rll3.model.procedural.GridFactory.Companion.cT
import com.ovle.rll3.model.procedural.GridFactory.Companion.fT
import com.ovle.rll3.model.procedural.GridFactory.Companion.wT
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray

interface GridFactory {
    companion object {
        const val wT = 1.0f
        const val fT = 0.6f
        const val cT = 0.2f
    }

    fun get(size: Int): Grid
}

class DungeonGridFactory: GridFactory {

    override fun get(size: Int): Grid {
        val grid = Grid(size)

        DungeonGenerator.getInstance().apply {
            roomGenerationAttempts = 100
            maxRoomSize = 7
            minRoomSize = 3
            tolerance = 10 // Max difference between width and height.

            wallThreshold = wT
            floorThreshold = fT
            corridorThreshold = cT

            windingChance = 0.25f
            randomConnectorChance = 0.05f
        }.generate(grid)

        return grid
    }
}

interface GridMapper<T> {

    fun map(grid: Grid): T

}

class GridToTileArrayMapper: GridMapper<TileArray> {

    override fun map(grid: Grid)=
            TileArray(grid.array.map { Tile(gridValueToTileId(it)) }.toTypedArray(), grid.width)

    private fun gridValueToTileId(gridValue: Float): Int {
        return when {
            //todo constants
            gridValue >= wT -> 1
            gridValue == fT -> 0
            gridValue == cT -> 2
            else -> -1
        }
    }
}

interface TileFactory {
    fun get(size: Int): TileArray
}

class MockTileFactory: TileFactory {

    override fun get(size: Int): TileArray {
        return TileArray(Array(size * size) { Tile() }, size)
    }
}

class TemplateTileFactory(private val template: Array<Array<Int>>): TileFactory {

    override fun get(size: Int): TileArray {
        return TileArray(Array(size * size) { tileIndexToTile(it, size) }, size)
    }

    private fun tileIndexToTile(index: Int, size: Int) = Tile(template[index % size][index / size])
}

/**
 *
 */
fun <T> createTiles(size: Int, gridFactory: GridFactory, mapper: GridMapper<T>): T = mapper.map(gridFactory.get(size))

/**
 *
 */
fun <T> createTiles(size: Int, factory: TileFactory) = factory.get(size)
