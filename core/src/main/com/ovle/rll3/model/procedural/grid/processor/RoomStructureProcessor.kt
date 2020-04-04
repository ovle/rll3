package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.ovle.rll3.model.ecs.component.special.LevelDescription
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.procedural.grid.RoomStructure
import com.ovle.rll3.model.tile.NearValues
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues

class RoomStructureProcessor : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine, levelDescription: LevelDescription) {
        levelInfo.rooms.forEach {
            processRoom(levelInfo.tiles, it)
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