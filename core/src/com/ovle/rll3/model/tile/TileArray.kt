package com.ovle.rll3.model.tile

import com.github.czyzby.noise4j.array.Object2dArray


class TileArray(
    tiles: Array<Tile>,
    val size: Int
): Object2dArray<Tile?>(tiles, size) {

    override fun getArray(size: Int): Array<Tile> = Array(size) { Tile() }
}