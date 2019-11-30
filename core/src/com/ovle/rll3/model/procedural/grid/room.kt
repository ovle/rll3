package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.procedural.roomFloorTypes
import com.ovle.rll3.model.tile.*

enum class RoomStructure {
    NoOp {
        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray) {}
    },
    Pit {
        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray) {
            val isFreeSpaceTile = nearTiles.all.all { it in roomFloorTypes }
            if (!isFreeSpaceTile) return

            setTile(tiles, nearTiles, pitFloorTileId)
        }
    },
    FilledCenter {
        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray) {
            val roomCenterX = room.x + room.width / 2
            val roomCenterY = room.y + room.height / 2
            val sizeCoef = 4    //todo
            val sizeX = room.width/ sizeCoef
            val sizeY = room.height/ sizeCoef
            val xRange = (roomCenterX - sizeX..roomCenterX + sizeX)
            val yRange = (roomCenterY - sizeY..roomCenterY + sizeY)
            val isColumn = nearTiles.x in xRange && nearTiles.y in yRange

            if (isColumn) {
                setTile(tiles, nearTiles, wallTileId)
            }
        }
    },
    Colonnade {
        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray) {
            val isFreeSpaceTile = nearTiles.nearHV.all { it in roomFloorTypes }
            if (!isFreeSpaceTile) return

            val isColumn = (nearTiles.x % 2 == 0 && (nearTiles.y == room.y + 1 || nearTiles.y == room.y + room.height - 1))
                || (nearTiles.y % 2 == 0 && (nearTiles.x == room.x + 1 || nearTiles.x == room.x + room.width - 1))
            if (isColumn) {
                setTile(tiles, nearTiles, wallTileId)
            }
        }
    };

    protected fun setTile(tiles: TileArray, nearTiles: NearTiles, tileId: Int) {
        tiles.set(nearTiles.y, nearTiles.x, Tile(tileId))
    }

    abstract fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray)
}