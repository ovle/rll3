package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.tile.*
import com.ovle.rll3.model.tile.NearTiles.Companion.nearTiles

interface TilesInfoPostProcessor {
    fun process(tiles: TilesInfo)
}


data class DoorInfo(val x: Int, val y: Int)

class DoorsTileArrayPostProcessor: TilesInfoPostProcessor {
    override fun process(tiles: TilesInfo) { tiles.infoDictionary[InfoDictionaryKey.Doors] = processDoors(tiles.tiles) }

    private fun processDoors(tiles: TileArray): Set<DoorInfo> {
        val result = mutableSetOf<DoorInfo>()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearTiles(tiles, x, y)

                val isCorridorFloor = nearTiles.tileId == corridorFloorTileId
                val isRoomFloorNearHorisontal = nearTiles.nearHorisontal.contains(roomFloorTileId)
                val isRoomFloorNearVertical = nearTiles.nearVertical.contains(roomFloorTileId)
                val isDoor = isCorridorFloor && (isRoomFloorNearHorisontal || isRoomFloorNearVertical)

                if (isDoor) {
                    result.add(DoorInfo(x, y))
                }
            }
        }
        return result
    }
}