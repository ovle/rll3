package com.ovle.rll3.model.procedural.grid

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.isNear
import com.ovle.rll3.model.procedural.floorTypes
import com.ovle.rll3.model.procedural.lightSourceChance
import com.ovle.rll3.model.procedural.roomFloorTypes
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.model.tile.NearTiles.Companion.nearTiles
import java.lang.Math.random

interface TilesInfoPostProcessor {
    fun process(tilesInfo: TilesInfo)
}


data class DoorInfo(val x: Int, val y: Int)

class DoorTilesInfoPostProcessor : TilesInfoPostProcessor {
    override fun process(tilesInfo: TilesInfo) {
        tilesInfo.infoDictionary[InfoDictionaryKey.Doors] = processDoors(tilesInfo.tiles)
    }

    private fun processDoors(tiles: TileArray): Set<DoorInfo> {
        val result = mutableSetOf<DoorInfo>()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearTiles(tiles, x, y)

                val isCorridorFloor = nearTiles.tileId == corridorFloorTileId
                val isRoomFloorNearHorisontal = nearTiles.nearH.contains(roomFloorTileId)
                val isRoomFloorNearVertical = nearTiles.nearV.contains(roomFloorTileId)
                val isDoor = isCorridorFloor && (isRoomFloorNearHorisontal || isRoomFloorNearVertical)

                if (isDoor) {
                    result.add(DoorInfo(x, y))
                }
            }
        }
        return result
    }
}

class RoomStructurePostProcessor : TilesInfoPostProcessor {
    override fun process(tilesInfo: TilesInfo) {
        processRooms(tilesInfo)
    }

    private fun processRooms(tiles: TilesInfo) {
        tiles.rooms().forEach {
            processRoom(tiles.tiles, it)
        }
    }

    private fun processRoom(tiles: TileArray, room: RoomInfo) {
        val roomStructure = RoomStructure.values().random()
        val params = roomStructure.initParams()
        for (x in room.x until room.x + room.width) {
            for (y in room.y until room.y + room.height) {
                val nearTiles = nearTiles(tiles, x, y)
                processRoomTile(nearTiles, room, tiles, roomStructure, params)
            }
        }
    }

    private fun processRoomTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, roomStructure: RoomStructure, params: Map<RoomStructure.ParamKey, Any>) {
        roomStructure.processTile(nearTiles, room, tiles, params)
    }
}

data class LightSourceInfo(val x: Int, val y: Int)

class LightSourceTilesInfoPostProcessor : TilesInfoPostProcessor {
    override fun process(tilesInfo: TilesInfo) {
        tilesInfo.infoDictionary[InfoDictionaryKey.Lights] = processLights(tilesInfo.tiles)
    }

    private fun processLights(tiles: TileArray): Set<LightSourceInfo> {
        val result = mutableSetOf<LightSourceInfo>()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearTiles(tiles, x, y)
                val isFloorTile = nearTiles.tileId in floorTypes
                val isFreeSpaceTileNear = nearTiles.allHV.any { it in floorTypes }
                val isWallTileNear = nearTiles.allHV.any { it == wallTileId }
                val isFreeForLightSource = isFloorTile && isFreeSpaceTileNear && isWallTileNear
                val isLightSource = isFreeForLightSource && random() <= lightSourceChance
                //todo check doors ?
                if (isLightSource) {
                    result.add(LightSourceInfo(x, y))
                }
            }
        }
        return result
    }
}

data class RoomInfo(val x: Int, val y: Int, val width: Int, val height: Int)
typealias RoomTiles = MutableList<Vector2>


class RoomsInfoPostProcessor : TilesInfoPostProcessor {
    override fun process(tilesInfo: TilesInfo) {
        tilesInfo.infoDictionary[InfoDictionaryKey.Rooms] = processRooms(tilesInfo.tiles)
    }

    private fun processRooms(tiles: TileArray): Set<RoomInfo>  {
        val roomsData = mutableListOf<RoomTiles>()
        var currentRoom: RoomTiles? = null
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearTiles(tiles, x, y)
                val isRoomTile = nearTiles.tileId in roomFloorTypes
                if (isRoomTile) {
                    if (currentRoom == null) {
                        currentRoom = roomsData.find {
                            coords -> coords.any { coord -> isNear(coord.x.toInt(), coord.y.toInt(), x, y) }
                        } ?: mutableListOf<Vector2>().apply { roomsData.add(this) }
                    }
                    currentRoom.add(Vector2(x.toFloat(), y.toFloat()))
                } else {
                    currentRoom = null
                }
            }
        }

        return roomsData.map {
            coords ->
            val minX = coords.minBy { it.x }!!.x.toInt()
            val minY = coords.minBy { it.y }!!.y.toInt()
            val maxX = coords.maxBy { it.x }!!.x.toInt()
            val maxY = coords.maxBy { it.y }!!.y.toInt()
            val room = RoomInfo(
                x = minX,
                y = minY,
                width = maxX - minX,
                height = maxY - minY
            )
            println(room)
            return@map room
        }.toSet()
    }


}