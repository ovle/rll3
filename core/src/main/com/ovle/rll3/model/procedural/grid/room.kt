package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.procedural.grid.RoomStructure.DirectionValue.*
import com.ovle.rll3.model.procedural.grid.RoomStructure.ParamKey.*
import com.ovle.rll3.model.procedural.grid.processor.RoomInfo
import com.ovle.rll3.model.tile.*

enum class RoomStructure {
    Nop {
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf()
        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {}
    },

    Pit {
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf(
            PitBridgeDirection to DirectionValue.values().random(r)
        )

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
            val isFreeSpaceTile = nearTiles.all.all { it?.typeId in roomFloorTypes }
            if (!isFreeSpaceTile) return

            var resultTileId = pitFloorTileId
            val dirValue = params[PitBridgeDirection]
            if (dirValue != NoDirection) {
                val haveHBridge = dirValue in setOf(H, HV)
                val haveVBridge = dirValue in setOf(V, HV)
                val isHBridgeTile = nearTiles.y == room.y + room.height / 2
                val isVBridgeTile = nearTiles.x == room.x + room.width / 2
                val isBridgeTile = haveHBridge && isHBridgeTile || haveVBridge && isVBridgeTile
                if (isBridgeTile) resultTileId = groundTileId
            }

            setTile(tiles, nearTiles, resultTileId)
        }
    },

//    todo issue with castle and rombic rooms
    FilledCenter {
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> {
            val isHollow = arrayOf(true, false).random(r)
            return mapOf(
                FilledCenterSize to arrayOf(3, 4, 5).random(r),
                FilledCenterHollowness to isHollow,
                FilledCenterPathDirection to DirectionValue.values().filter { !isHollow || it != NoDirection }.random(r)
            )
        }

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
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
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf(ColonnadeDirection to DirectionValue.values().random(r))

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
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
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf(
            RandomNoiseAmount to r.nextFloat()
        )

        override fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
            val amount = params[RandomNoiseAmount] as Float
            if (r.nextDouble() >= amount) return

            val tileId = arrayOf(wallTileId, pitFloorTileId).random(r)

            setTile(tiles, nearTiles, tileId)
        }
    };

    protected fun setTile(tiles: TileArray, nearTiles: NearTiles, tileId: TileType) {
        val x = nearTiles.x
        val y = nearTiles.y

        tiles.setTile(x, y, Tile(tileId))
    }

    abstract fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any>

    abstract fun processTile(nearTiles: NearTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random)


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