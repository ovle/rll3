package com.ovle.rll3.model.tile

import com.badlogic.gdx.math.GridPoint2


class TileArray(
    val tiles: Array<Tile>,
    val size: Int
) {
    fun index(x: Int, y: Int) = size * y + x
    fun point(index: Int) = GridPoint2(
        index % size,
//        index / size
        size - 1 - index / size
    )

    fun tile(x: Int, y: Int) = tiles[index(x, y)]
    fun setTile(x: Int, y: Int, tile: Tile) {
        tiles[index(x, y)] = tile
    }

    fun indexedTiles() = tiles.mapIndexed { index, tile -> index to tile }
    fun positions() = tiles.mapIndexed { index, _ -> point(index) }

    fun isPointValid(x: Int, y: Int) = x in (0 until size) && y in (0 until size)
}