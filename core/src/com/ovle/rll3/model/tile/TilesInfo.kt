package com.ovle.rll3.model.tile

import com.ovle.rll3.model.procedural.grid.DoorInfo
import com.ovle.rll3.model.procedural.grid.LightSourceInfo
import com.ovle.rll3.model.procedural.grid.RoomInfo


data class TilesInfo(val tiles: TileArray, val infoDictionary: MutableMap<InfoDictionaryKey, Any> = mutableMapOf()) {
    private fun doorsInfo() = infoDictionary[InfoDictionaryKey.Doors] as Set<DoorInfo>? ?: setOf()
    private fun lightsInfo() = infoDictionary[InfoDictionaryKey.Lights] as Set<LightSourceInfo>? ?: setOf()
    private fun roomsInfo() = infoDictionary[InfoDictionaryKey.Rooms] as Set<RoomInfo>? ?: setOf()

    fun hasDoor(x: Int, y: Int) = doorsInfo().any { it.x == x && it.y == y }
    fun hasLight(x: Int, y: Int) = lightsInfo().any { it.x == x && it.y == y }
    fun rooms() = roomsInfo()
    fun lights() = lightsInfo()
}
