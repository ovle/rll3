package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.ecs.component.point
import com.ovle.rll3.model.procedural.grid.RoomStructure.DirectionValue.*
import com.ovle.rll3.model.procedural.grid.RoomStructure.ParamKey.*
import com.ovle.rll3.model.procedural.grid.processor.RoomInfo
import com.ovle.rll3.model.procedural.roomFloorTypes
import com.ovle.rll3.model.tile.*
import java.lang.Math.random

enum class RoomStructure {
    Nop {
        override fun initParams(room: RoomInfo): Map<ParamKey, Any> = mapOf()
        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>) {}
    },

    Pit {
        override fun initParams(room: RoomInfo): Map<ParamKey, Any> = mapOf(
            PitBridgeDirection to DirectionValue.values().random()
        )

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>) {
            val isFreeSpaceTile = nearTiles.all.all { it?.typeId in roomFloorTypes }
            if (!isFreeSpaceTile) return

            val dirValue = params[PitBridgeDirection]
            if (dirValue != NoDirection) {
                val haveHBridge = dirValue in setOf(H, HV)
                val haveVBridge = dirValue in setOf(V, HV)
                val isHBridgeTile = nearTiles.y == room.y + room.height / 2
                val isVBridgeTile = nearTiles.x == room.x + room.width / 2
                val isBridgeTile = haveHBridge && isHBridgeTile || haveVBridge && isVBridgeTile
                if (isBridgeTile) return
            }

            setTile(tiles, nearTiles, pitFloorTileId)
        }
    },

//    todo issue with castle and rombic rooms
    FilledCenter {
        override fun initParams(room: RoomInfo): Map<ParamKey, Any> {
            val isHollow = arrayOf(true, false).random()
            return mapOf(
                FilledCenterSize to arrayOf(3, 4, 5).random(),
                FilledCenterHollowness to isHollow,
                FilledCenterPathDirection to DirectionValue.values().filter { !isHollow || it != NoDirection }.random()
            )
        }

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>) {
            val sizeCoef = params[FilledCenterSize] as Int
            val isHollow = params[FilledCenterHollowness] as Boolean
            val dirValue =  params[FilledCenterPathDirection]

            val roomCenterX = room.x + room.width / 2
            val roomCenterY = room.y + room.height / 2
            val sizeX = room.width/ sizeCoef
            val sizeY = room.height/ sizeCoef
            val xRange = (roomCenterX - sizeX..roomCenterX + sizeX)
            val yRange = (roomCenterY - sizeY..roomCenterY + sizeY)

            val xInRange = nearTiles.x in xRange
            val yInRange = nearTiles.y in yRange
            val isColumn = if (!isHollow) xInRange && yInRange
            else {
                val isVWall = (nearTiles.x == xRange.first || nearTiles.x == xRange.last) && yInRange
                val isHWall = (nearTiles.y == yRange.first || nearTiles.y == yRange.last) && xInRange
                isVWall || isHWall
            }

            val isHPathTile = nearTiles.y == room.y + room.height / 2
            val isVPathTile = nearTiles.x == room.x + room.width / 2
            val haveHPath = dirValue in setOf(H, HV)
            val haveVPath = dirValue in setOf(V, HV)
            val isPathTile = haveHPath && isHPathTile || haveVPath && isVPathTile
            if (isPathTile) return

            if (isColumn) {
                setTile(tiles, nearTiles, wallTileId)
            }
        }
    },

    Colonnade {
        override fun initParams(room: RoomInfo): Map<ParamKey, Any> = mapOf(ColonnadeDirection to DirectionValue.values().random())

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>) {
            val dirValue = params[ColonnadeDirection]
            val isFreeSpaceTile = nearTiles.nearHV.all { it?.typeId in roomFloorTypes }
            if (!isFreeSpaceTile) return

            val isHColumn = nearTiles.x % 2 == 0 && (nearTiles.y == room.y + 1 || nearTiles.y == room.y + room.height - 1)
            val isVColumn = nearTiles.y % 2 == 0 && (nearTiles.x == room.x + 1 || nearTiles.x == room.x + room.width - 1)
            val isColumn = isHColumn && (dirValue in setOf(H, HV))
                        || isVColumn && (dirValue in setOf(V, HV))
                        || isHColumn && isVColumn && dirValue == NoDirection
            if (isColumn) {
                setTile(tiles, nearTiles, wallTileId)
            }
        }
    },

    Random {
        override fun initParams(room: RoomInfo): Map<ParamKey, Any> = mapOf(
            RandomNoiseAmount to random().toFloat()
        )

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>) {
            val amount = params[RandomNoiseAmount] as Float
            if (random() >= amount) return

            val tileId = arrayOf(wallTileId, pitFloorTileId).random()

            setTile(tiles, nearTiles, tileId)
        }
    };


    protected fun setTile(tiles: TileArray, nearTiles: NearTiles, tileId: Int) {
        val x = nearTiles.x
        val y = nearTiles.y
        //todo tile position?
        tiles.set(x, y, Tile(point(x, y), tileId))
    }

    abstract fun initParams(room: RoomInfo): Map<ParamKey, Any>

    abstract fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>)


    enum class DirectionValue {
        H, V, HV, NoDirection
    }

    enum class ParamKey {
        ColonnadeDirection,
        FilledCenterSize,
        FilledCenterHollowness,
        FilledCenterPathDirection,
        PitBridgeDirection,
        RandomNoiseAmount
    }
}