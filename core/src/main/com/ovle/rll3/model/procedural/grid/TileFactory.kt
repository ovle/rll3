package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.TileType
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray

//todo
interface TileFactory {
    fun get(size: Int): TileArray
}

class MockTileFactory: TileFactory {

    override fun get(size: Int): TileArray {
        return TileArray(Array(size * size) { Tile() }, size)
    }
}

class TemplateTileFactory(private val template: Array<Array<TileType>>): TileFactory {

    override fun get(size: Int): TileArray {
        return TileArray(Array(size * size) { tileIndexToTile(it, size) }, size)
    }

    private fun tileIndexToTile(index: Int, size: Int) = Tile(typeId = template[index % size][index / size])
}