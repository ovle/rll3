package com.ovle.rll3.model.tile

import com.github.czyzby.noise4j.array.Object2dArray


data class Tile(val typeId: Int = -1)

enum class InfoDictionaryKey {
    Doors,
    Rooms,
    Exits
}

class TileArray(
    tiles: Array<Tile>,
    size: Int,
    val infoDictionary: Map<InfoDictionaryKey, Any> = mapOf()
): Object2dArray<Tile>(tiles, size) {

    override fun getArray(size: Int): Array<Tile> = Array(size) { Tile() }

}