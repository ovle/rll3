package com.ovle.rll3.model.tile
import com.github.czyzby.noise4j.array.Object2dArray
import com.ovle.rll3.model.procedural.grid.DoorInfo

const val roomFloorTileId = 0
const val wallTileId = 1
const val corridorFloorTileId = 2
const val outOfMapTileId = wallTileId

data class Tile(val typeId: Int = -1)
enum class InfoDictionaryKey {
    Doors,
    Rooms,
    Exits
}

class TileArray(
    tiles: Array<Tile>,
    size: Int
): Object2dArray<Tile>(tiles, size) {

    override fun getArray(size: Int): Array<Tile> = Array(size) { Tile() }
}

data class TilesInfo(val tiles: TileArray, val infoDictionary: MutableMap<InfoDictionaryKey, Any> = mutableMapOf()) {
    fun doorsInfo() = infoDictionary[InfoDictionaryKey.Doors] as Set<DoorInfo>?
}