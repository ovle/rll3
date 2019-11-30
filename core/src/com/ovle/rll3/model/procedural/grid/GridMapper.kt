package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.tile.*


interface GridMapper<T> {
    fun map(gridWrapper: GridWrapper): T
}

class GridToTileArrayMapper: GridMapper<TileArray> {

    override fun map(gridWrapper: GridWrapper): TileArray {
        val grid = gridWrapper.grid
        val tiles = grid.array.map { Tile(gridValueToTileId(it)) }.toTypedArray()
        val size = grid.width
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