package com.ovle.rll3.model.procedural.grid.processor.location.room

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.ovle.rlUtil.gdx.math.adjTiles
import com.ovle.rll3.RoomTiles
import com.ovle.rlUtil.gdx.math.isAdj
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import com.ovle.rll3.model.procedural.config.location.floorTypes
import kotlin.math.roundToInt


//todo component ?
data class RoomInfo(val x: Int, val y: Int, val width: Int, val height: Int)


class RoomsInfoProcessor : LocationProcessor {

    override fun process(locationInfo: LocationInfo, gameEngine: Engine) {
        val tiles = locationInfo.tiles
        val roomsData = mutableListOf<RoomTiles>()
        var currentRoom: RoomTiles? = null
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val adjTiles = adjTiles(tiles, x, y)
                val isRoomTile = adjTiles.value in floorTypes
                if (isRoomTile) {
                    if (currentRoom == null) {
                        //todo refactor
                        currentRoom = roomsData.find {
                            coords -> coords.any { coord -> isAdj(coord.x.roundToInt(), coord.y.roundToInt(), x, y) }
                        } ?: mutableListOf<Vector2>().apply { roomsData.add(this) }
                    }
                    currentRoom.add(Vector2(x.toFloat(), y.toFloat()))
                } else {
                    currentRoom = null
                }
            }
        }

        val result = roomsData.map {
            coords ->
            val minX = coords.minBy { it.x }!!.x.roundToInt()
            val minY = coords.minBy { it.y }!!.y.roundToInt()
            val maxX = coords.maxBy { it.x }!!.x.roundToInt()
            val maxY = coords.maxBy { it.y }!!.y.roundToInt()
            val room = RoomInfo(
                x = minX,
                y = minY,
                width = maxX - minX,
                height = maxY - minY
            )
//            println(room)
            return@map room
        }

        locationInfo.rooms.addAll(result)
    }
}