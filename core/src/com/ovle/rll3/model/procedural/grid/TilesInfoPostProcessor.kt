package com.ovle.rll3.model.procedural.grid

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.isNear
import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.procedural.floorTypes
import com.ovle.rll3.model.procedural.lightSourceChance
import com.ovle.rll3.model.procedural.roomFloorTypes
import com.ovle.rll3.model.tile.*
import java.lang.Math.random


//todo need rework
interface TilesInfoPostProcessor {

    fun process(levelInfo: LevelInfo, gameEngine: GameEngine) {
        levelInfo.objects.plusAssign(process(levelInfo.tiles, gameEngine))
    }

    fun process(tiles: TileArray, gameEngine: GameEngine): Collection<Entity> {
        throw UnsupportedOperationException("")
    }
}


class DoorTilesInfoPostProcessor : TilesInfoPostProcessor {

    override fun process(tiles: TileArray, gameEngine: GameEngine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearValues(tiles, x, y)

                val isCorridorFloor = nearTiles.value?.typeId == corridorFloorTileId
                val isRoomFloorNearHorisontal = roomFloorTileId in nearTiles.nearH.mapNotNull { it?.typeId }
                val isRoomFloorNearVertical = roomFloorTileId in nearTiles.nearV.mapNotNull { it?.typeId }
                val isDoor = isCorridorFloor && (isRoomFloorNearHorisontal || isRoomFloorNearVertical)

                if (isDoor) {
                    result.add(door(x, y, gameEngine))
                }
            }
        }
        return result
    }

    private fun door(x: Int, y: Int, gameEngine: GameEngine): Entity {
        return gameEngine.entity(
            PositionComponent(Vector2(x.toFloat(), y.toFloat())),
            DoorComponent()
        )
    }
}

class RoomStructurePostProcessor : TilesInfoPostProcessor {
    override fun process(levelInfo: LevelInfo,  gameEngine: GameEngine) {
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

class LightSourceTilesInfoPostProcessor : TilesInfoPostProcessor {

    override fun process(tiles: TileArray, gameEngine: GameEngine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearValues(tiles, x, y)
                val isFloorTile = nearTiles.value?.typeId in floorTypes
                val isFreeSpaceTileNear = nearTiles.allHV.map { it?.typeId }.any { it in floorTypes }
                val isWallTileNear = nearTiles.allHV.map { it?.typeId }.any { it == wallTileId }
                val isFreeForLightSource = isFloorTile && isFreeSpaceTileNear && isWallTileNear
                val isLightSource = isFreeForLightSource && random() <= lightSourceChance
                //todo check doors ?
                if (isLightSource) {
                    result.add(lightSource(x, y, gameEngine))
                }
            }
        }
        return result
    }

    private fun lightSource(x: Int, y: Int, gameEngine: GameEngine): Entity {
        return gameEngine.entity(
            PositionComponent(Vector2(x.toFloat(), y.toFloat())),
            LightComponent(5)
        )
    }
}

//todo component ?
data class RoomInfo(val x: Int, val y: Int, val width: Int, val height: Int)
typealias RoomTiles = MutableList<Vector2>

class RoomsInfoPostProcessor : TilesInfoPostProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: GameEngine) {
        val tiles = levelInfo.tiles
        val roomsData = mutableListOf<RoomTiles>()
        var currentRoom: RoomTiles? = null
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearValues(tiles, x, y)
                val isRoomTile = nearTiles.value?.typeId in roomFloorTypes
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

        val result = roomsData.map { coords ->
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
        }

        levelInfo.rooms.addAll(result)
    }
}