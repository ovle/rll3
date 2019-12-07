package com.ovle.rll3.model.procedural.grid

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.tile.*


interface GridMapper<T> {
    fun map(grid: Grid): T
}

class GridToTileArrayMapper: GridMapper<TileArray> {

    override fun map(grid: Grid): TileArray {
        val size = grid.width
        val tiles = grid.array.mapIndexed {
            index, value -> Tile(
                x = index / size,   //todo
                y = index % size,
                typeId = gridValueToTileId(value)
            )
        }.toTypedArray()
        return TileArray(tiles, size)
    }

    private fun gridValueToTileId(gridValue: Float): Int {
        return when {
            gridValue >= GridFactory.wallTreshold -> wallTileId
            gridValue == GridFactory.floorTreshold -> roomFloorTileId
            gridValue == GridFactory.corridorTreshold -> corridorFloorTileId
            else -> -1
        }
    }
}