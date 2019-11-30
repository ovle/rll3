package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.procedural.floorTypes
import com.ovle.rll3.model.procedural.lightSourceChance
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
        for (x in room.x until room.x + room.width) {
            for (y in room.y until room.y + room.height) {
                val nearTiles = nearTiles(tiles, x, y)
                processRoomTile(nearTiles, room, tiles, roomStructure)
            }
        }
    }

    private fun processRoomTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, roomStructure: RoomStructure) {
        roomStructure.processTile(nearTiles, room, tiles)
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

//todo get from scratch
class RoomsInfoPostProcessor : TilesInfoPostProcessor {
    override fun process(tilesInfo: TilesInfo) {
        tilesInfo.infoDictionary[InfoDictionaryKey.Rooms] = processRooms(tilesInfo)
    }

    private fun processRooms(tiles: TilesInfo): Set<RoomInfo>  {
        return tiles.source.rooms.map { RoomInfo(x = it.x, y = it.y, width = it.width, height = it.height) }.toSet()
    }
}