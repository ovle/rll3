package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.ovle.rll3.TileArray
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.procedural.grid.RoomStructure
import com.ovle.rll3.model.tile.nearValues
import kotlin.random.Random

class RoomStructureProcessor : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine) {
        val random = levelInfo.random.kRandom
        levelInfo.rooms.forEach {
            processRoom(levelInfo.tiles, it, random)
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