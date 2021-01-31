package com.ovle.rll3.model.procedural.grid.processor.location.room

import com.ovle.rlUtil.AdjTiles
import com.ovle.rlUtil.Tile
import com.ovle.rlUtil.TileArray
import com.ovle.rll3.model.procedural.config.location.floorTypes
import com.ovle.rll3.model.procedural.config.location.highGroundTileId
import com.ovle.rll3.model.procedural.config.location.naturalHighWallTileId
import com.ovle.rll3.model.procedural.config.location.pitFloorTileId
import com.ovle.rll3.model.procedural.grid.processor.location.room.RoomStructure.DirectionValue.*
import com.ovle.rll3.model.procedural.grid.processor.location.room.RoomStructure.ParamKey.*

enum class RoomStructure {
    Nop {
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf()
        override fun processTile(adjTiles: AdjTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {}
    },

    Pit {
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf(
            PitBridgeDirection to DirectionValue.values().random(r)
        )

        override fun processTile(adjTiles: AdjTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
            val isFreeSpaceTile = adjTiles.all.all { it in floorTypes }
            if (!isFreeSpaceTile) return

            var resultTileId = pitFloorTileId
            val dirValue = params[PitBridgeDirection]
            if (dirValue != NoDirection) {
                val haveHBridge = dirValue in setOf(H, HV)
                val haveVBridge = dirValue in setOf(V, HV)
                val isHBridgeTile = adjTiles.y == room.y + room.height / 2
                val isVBridgeTile = adjTiles.x == room.x + room.width / 2
                val isBridgeTile = haveHBridge && isHBridgeTile || haveVBridge && isVBridgeTile
                if (isBridgeTile) resultTileId = highGroundTileId
            }

            setTile(tiles, adjTiles, resultTileId)
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

        override fun processTile(adjTiles: AdjTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
            val sizeCoef = params[FilledCenterSize] as Int
            val isHollow = params[FilledCenterHollowness] as Boolean
            val dirValue =  params[FilledCenterPathDirection]

            val roomCenterX = room.x + room.width / 2
            val roomCenterY = room.y + room.height / 2
            val sizeX = room.width/ sizeCoef
            val sizeY = room.height/ sizeCoef
            val xRange = (roomCenterX - sizeX..roomCenterX + sizeX)
            val yRange = (roomCenterY - sizeY..roomCenterY + sizeY)

            val xInRange = adjTiles.x in xRange
            val yInRange = adjTiles.y in yRange
            val isColumn = if (!isHollow) xInRange && yInRange
            else {
                val isVWall = (adjTiles.x == xRange.first || adjTiles.x == xRange.last) && yInRange
                val isHWall = (adjTiles.y == yRange.first || adjTiles.y == yRange.last) && xInRange
                isVWall || isHWall
            }

            val isHPathTile = adjTiles.y == room.y + room.height / 2
            val isVPathTile = adjTiles.x == room.x + room.width / 2
            val haveHPath = dirValue in setOf(H, HV)
            val haveVPath = dirValue in setOf(V, HV)
            val isPathTile = haveHPath && isHPathTile || haveVPath && isVPathTile
            if (isPathTile) return

            if (isColumn) {
                setTile(tiles, adjTiles, naturalHighWallTileId)
            }
        }
    },

    Colonnade {
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf(ColonnadeDirection to DirectionValue.values().random(r))

        override fun processTile(adjTiles: AdjTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
            val dirValue = params[ColonnadeDirection]
            val isFreeSpaceTile = adjTiles.nearHV.all { it in floorTypes }
            if (!isFreeSpaceTile) return

            val isHColumn = adjTiles.x % 2 == 0 && (adjTiles.y == room.y + 1 || adjTiles.y == room.y + room.height - 1)
            val isVColumn = adjTiles.y % 2 == 0 && (adjTiles.x == room.x + 1 || adjTiles.x == room.x + room.width - 1)
            val isColumn = isHColumn && (dirValue in setOf(H, HV))
                        || isVColumn && (dirValue in setOf(V, HV))
                        || isHColumn && isVColumn && dirValue == NoDirection
            if (isColumn) {
                setTile(tiles, adjTiles, naturalHighWallTileId)
            }
        }
    },

    Random {
        override fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any> = mapOf(
            RandomNoiseAmount to r.nextFloat()
        )

        override fun processTile(adjTiles: AdjTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random) {
            val amount = params[RandomNoiseAmount] as Float
            if (r.nextDouble() >= amount) return

            val tile = arrayOf(naturalHighWallTileId, pitFloorTileId).random(r)

            setTile(tiles, adjTiles, tile)
        }
    };

    protected fun setTile(tiles: TileArray, adjTiles: AdjTiles, tile: Tile) {
        val x = adjTiles.x
        val y = adjTiles.y

        tiles.set(x, y, tile)
    }

    abstract fun initParams(room: RoomInfo, r: kotlin.random.Random): Map<ParamKey, Any>

    abstract fun processTile(adjTiles: AdjTiles, room: RoomInfo, tiles: TileArray, params: Map<ParamKey, Any>, r: kotlin.random.Random)


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