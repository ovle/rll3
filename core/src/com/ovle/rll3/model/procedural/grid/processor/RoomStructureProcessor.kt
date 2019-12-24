package com.ovle.rll3.model.procedural.grid.processor

import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.procedural.grid.RoomStructure
import com.ovle.rll3.model.tile.*

class RoomStructureProcessor : TilesInfoProcessor {
    override fun process(levelInfo: LevelInfo, gameEngine: GameEngine) {
        processRooms(levelInfo)
    }

    private fun processRooms(level: LevelInfo) {
        level.rooms.forEach {
            processRoom(level.tiles, it)
        }
    }

    private fun processRoom(tiles: TileArray, room: RoomInfo) {
        val roomStructure = RoomStructure.values().filter { it != RoomStructure.Random }.random()
        val params = roomStructure.initParams(room)
        for (x in room.x until room.x + room.width) {
            for (y in room.y until room.y + room.height) {
                val nearTiles = nearValues(tiles, x, y)
                processRoomTile(nearTiles, room, tiles, roomStructure, params)
            }
        }
    }

    private fun processRoomTile(nearTiles: NearValues<Tile?>, room: RoomInfo, tiles: TileArray, roomStructure: RoomStructure, params: Map<RoomStructure.ParamKey, Any>) {
        roomStructure.processTile(nearTiles, room, tiles, params)
    }
}