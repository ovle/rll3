package com.ovle.rll3.model.procedural.grid.processor.location.room

import com.badlogic.ashley.core.Engine
import com.ovle.rlUtil.TileArray
import com.ovle.rlUtil.gdx.math.adjTiles
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import kotlin.random.Random

class RoomStructureProcessor : LocationProcessor {

    override fun process(locationInfo: LocationInfo, gameEngine: Engine) {
        val random = locationInfo.random.kRandom
        locationInfo.rooms.forEach {
            processRoom(locationInfo.tiles, it, random)
        }
    }

    private fun processRoom(tiles: TileArray, room: RoomInfo, r: Random) {
        val roomStructure = RoomStructure.values().filter { it != RoomStructure.Random }.random(r)
        val params = roomStructure.initParams(room, r)
        for (x in room.x until room.x + room.width) {
            for (y in room.y until room.y + room.height) {
                val adjTiles = adjTiles(tiles, x, y)
                roomStructure.processTile(adjTiles, room, tiles, params, r)
            }
        }
    }
}