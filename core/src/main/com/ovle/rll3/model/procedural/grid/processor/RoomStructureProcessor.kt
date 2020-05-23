package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.procedural.grid.RoomStructure
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues
import kotlin.random.Random

class RoomStructureProcessor : TilesProcessor {

    override fun process(levelInfo: LevelInfo, worldInfo: WorldInfo, gameEngine: Engine) {
        val r = worldInfo.r
        levelInfo.rooms.forEach {
            processRoom(levelInfo.tiles, it, r)
        }
    }

    private fun processRoom(tiles: TileArray, room: RoomInfo, r: Random) {
        val roomStructure = RoomStructure.values().filter { it != RoomStructure.Random }.random(r)
        val params = roomStructure.initParams(room, r)
        for (x in room.x until room.x + room.width) {
            for (y in room.y until room.y + room.height) {
                val nearTiles = nearValues(tiles, x, y)
                roomStructure.processTile(nearTiles, room, tiles, params, r)
            }
        }
    }
}