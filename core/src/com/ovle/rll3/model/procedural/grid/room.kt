package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.procedural.grid.RoomStructure.ParamKey.ColonnadeDirection
import com.ovle.rll3.model.procedural.roomFloorTypes
import com.ovle.rll3.model.tile.NearTiles
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.wallTileId

enum class RoomStructure {
//    NoOp {
//        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray) {}
//    },
//    Pit {
//        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray) {
//            val isFreeSpaceTile = nearTiles.all.all { it in roomFloorTypes }
//            if (!isFreeSpaceTile) return
//
//            setTile(tiles, nearTiles, pitFloorTileId)
//        }
//    },
//    FilledCenter {
//        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray) {
////            val isFreeSpaceTile = nearTiles.all.all { it in roomFloorTypes }
////            if (!isFreeSpaceTile) return
//
//            val roomCenterX = room.x + room.width / 2
//            val roomCenterY = room.y + room.height / 2
//            val sizeCoef = 4    //todo
//            val sizeX = room.width/ sizeCoef
//            val sizeY = room.height/ sizeCoef
//            val xRange = (roomCenterX - sizeX..roomCenterX + sizeX)
//            val yRange = (roomCenterY - sizeY..roomCenterY + sizeY)
//            val isColumn = nearTiles.x in xRange && nearTiles.y in yRange
//
//            if (isColumn) {
//                setTile(tiles, nearTiles, wallTileId)
//            }
//        }
//    },

    Colonnade {
        override fun initParams(): Map<ParamKey, Any> = mapOf(ColonnadeDirection to ColonnadeDirValue.values().random())

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>) {
            val dirValue = params[ColonnadeDirection]
            val isFreeSpaceTile = nearTiles.nearHV.all { it in roomFloorTypes }
            if (!isFreeSpaceTile) return

            val isHColumn = nearTiles.x % 2 == 0 && (nearTiles.y == room.y + 1 || nearTiles.y == room.y + room.height - 1)
            val isVColumn = nearTiles.y % 2 == 0 && (nearTiles.x == room.x + 1 || nearTiles.x == room.x + room.width - 1)
            val isColumn = isHColumn && (dirValue in setOf(ColonnadeDirValue.H, ColonnadeDirValue.HV))
                        || isVColumn && (dirValue in setOf(ColonnadeDirValue.V, ColonnadeDirValue.HV))
            if (isColumn) {
                setTile(tiles, nearTiles, wallTileId)
            }
        }
    };

//    Random {
//        override fun initParams(): Map<ParamKey, Any> = mapOf(
//            RandomNoiseAmountPit to random(),
//            RandomNoiseAmountWall to random()
//        )
//
//        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//    }
//    ;

    protected fun setTile(tiles: TileArray, nearTiles: NearTiles, tileId: Int) {
        tiles.set(nearTiles.y, nearTiles.x, Tile(tileId))
    }

    abstract fun initParams(): Map<ParamKey, Any>

    abstract fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>)


    enum class ColonnadeDirValue {
        H, V, HV
    }

    enum class ParamKey {
        ColonnadeDirection,
        FilledCenterSize,
        FilledCenterEmpty,
        PitSize,
        PitBridgeDirection,
        RandomNoiseAmountPit,
        RandomNoiseAmountWall
    }
}