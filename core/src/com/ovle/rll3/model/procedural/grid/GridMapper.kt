package com.ovle.rll3.model.procedural.grid

import com.badlogic.gdx.math.Vector2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.tile.InfoDictionaryKey
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray


interface GridMapper<T> {
    fun map(grid: Grid): T
}

class GridToTileArrayMapper: GridMapper<TileArray> {

    override fun map(grid: Grid): TileArray {
        val tiles = grid.array.map { Tile(gridValueToTileId(it)) }.toTypedArray()
        return TileArray(tiles, grid.width, additionalInfo(tiles))
    }

    //todo move post processing to separate class
    private fun additionalInfo(tiles: Array<Tile>): Map<InfoDictionaryKey, Any> =
        mapOf(InfoDictionaryKey.Doors to doors(tiles))

    private fun doors(tiles: Array<Tile>): Array<Vector2> {
        return arrayOf()
    }

    private fun gridValueToTileId(gridValue: Float): Int {
        return when {
            //todo constants
            gridValue >= GridFactory.wallTreshold -> 1
            gridValue == GridFactory.floorTreshold -> 0
            gridValue == GridFactory.corridorTreshold -> 2
            else -> -1
        }
    }
}