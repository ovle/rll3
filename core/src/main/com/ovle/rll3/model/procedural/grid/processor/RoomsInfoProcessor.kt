package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.isNear
import com.ovle.rll3.model.ecs.component.special.LevelDescription
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTypes
import kotlin.math.roundToInt

typealias RoomTiles = MutableList<Vector2>

//todo component ?
data class RoomInfo(val x: Int, val y: Int, val width: Int, val height: Int)


class RoomsInfoProcessor : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine, levelDescription: LevelDescription) {
        val tiles = levelInfo.tiles
        val roomsData = mutableListOf<RoomTiles>()
        var currentRoom: RoomTiles? = null
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)
                val isRoomTile = nearTiles.value?.typeId in roomFloorTypes
                if (isRoomTile) {
                    if (currentRoom == null) {
                        //todo refactor
                        currentRoom = roomsData.find {
                            coords -> coords.any { coord -> isNear(coord.x.roundToInt(), coord.y.roundToInt(), x, y) }
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

        levelInfo.rooms.addAll(result)
    }
}