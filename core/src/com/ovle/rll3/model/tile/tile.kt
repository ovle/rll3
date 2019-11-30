package com.ovle.rll3.model.tile
import com.github.czyzby.noise4j.array.Object2dArray
import com.ovle.rll3.model.procedural.grid.DoorInfo
import com.ovle.rll3.model.procedural.grid.GridWrapper
import com.ovle.rll3.model.procedural.grid.LightSourceInfo
import com.ovle.rll3.model.procedural.grid.RoomInfo

const val roomFloorTileId = 0
const val wallTileId = 1
const val corridorFloorTileId = 2
const val pitFloorTileId = 3
const val outOfMapTileId = wallTileId

data class Tile(val typeId: Int = -1)
enum class InfoDictionaryKey {
    Doors,
    Lights,
    Rooms,
    Exits
}

class TileArray(
    tiles: Array<Tile>,
    size: Int
): Object2dArray<Tile>(tiles, size) {

    override fun getArray(size: Int): Array<Tile> = Array(size) { Tile() }
}

//todo
data class TilesInfo(val tiles: TileArray, val source: GridWrapper, val infoDictionary: MutableMap<InfoDictionaryKey, Any> = mutableMapOf()) {
    private fun doorsInfo() = infoDictionary[InfoDictionaryKey.Doors] as Set<DoorInfo>? ?: setOf()
    private fun lightsInfo() = infoDictionary[InfoDictionaryKey.Lights] as Set<LightSourceInfo>? ?: setOf()
    private fun roomsInfo() = infoDictionary[InfoDictionaryKey.Rooms] as Set<RoomInfo>? ?: setOf()

    fun hasDoor(x: Int, y: Int) = doorsInfo().any { it.x == x && it.y == y }
    fun hasLight(x: Int, y: Int) = lightsInfo().any { it.x == x && it.y == y }
    fun rooms() = roomsInfo()
    fun lights() = lightsInfo()
}