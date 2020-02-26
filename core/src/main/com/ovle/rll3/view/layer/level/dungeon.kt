package com.ovle.rll3.view.layer.level

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.TrapComponent
import com.ovle.rll3.model.ecs.hasEntityOnPosition
import com.ovle.rll3.model.procedural.grid.corridorFloorTypes
import com.ovle.rll3.model.procedural.grid.roomFloorTypes
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.point
import com.ovle.rll3.view.layer.floorBorderTileSet
import com.ovle.rll3.view.layer.portalTR
import com.ovle.rll3.view.layer.roomWallTileSet
import com.ovle.rll3.view.layer.trapsTR
import com.ovle.rll3.view.noLightning


fun dungeonTileToTexture(params: TileToTextureParams): kotlin.Array<TextureRegion> {
    val (layerType, nearTiles, textureRegions, levelInfo, lightInfo) = params

    val position = point(nearTiles.x, nearTiles.y)
    val positionDown = point(nearTiles.x, nearTiles.y - 1)

    fun hasDoor(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), DoorComponent::class)
    fun hasTrap(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), TrapComponent::class)
    fun hasTransition(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), LevelConnectionComponent::class)
    fun hasWall(tileId: Int?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId || hasDoor(x, y))) 1 else 0
    fun hasWallOrPit(tileId: Int?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId || tileId == pitFloorTileId || hasDoor(x, y))) 1 else 0
    fun hasRoomWall(tileId: Int?): Int = if (tileId != null && tileId != roomFloorTileId) 1 else 0

    val upTileId = nearTiles.upValue?.typeId
    val downTileId = nearTiles.downValue?.typeId
    val rightTileId = nearTiles.rightValue?.typeId
    val leftTileId = nearTiles.leftValue?.typeId

    val wallTileIndex = nearTiles.run {
        hasWall(rightTileId, x + 1, y) + 2 * hasWall(downTileId, x, y + 1) + 4 * hasWall(leftTileId, x - 1, y) + 8 * hasWall(upTileId, x, y - 1)
    }
    val tilesInSet = floorBorderTileSet.size * floorBorderTileSet.size - 1
    val floorBorderTileIndex = tilesInSet - nearTiles.run {
        hasWallOrPit(rightTileId, x + 1, y) + 2 * hasWallOrPit(downTileId, x, y + 1) + 4 * hasWallOrPit(leftTileId, x - 1, y) + 8 * hasWallOrPit(upTileId, x, y - 1)
    }

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == roomFloorTileId
    val isPitFloor = tileId == pitFloorTileId
    val isCorridorFloor = tileId == corridorFloorTileId
    val isDoor = hasDoor(nearTiles.x, nearTiles.y)
    val isNextToDoor = hasDoor(nearTiles.x, nearTiles.y - 1)
    val isRoomWall = upTileId in roomFloorTypes
    val isCorridorWall = upTileId in corridorFloorTypes
    val isDoorUp = hasDoor(nearTiles.x, nearTiles.y - 1)
    val isRoomFloorUp = downTileId == roomFloorTileId
    val isPitFloorUp = downTileId == pitFloorTileId
    val isRoomFloorNearVertical = roomFloorTileId in nearTiles.nearV.map { it?.typeId }

    val isTrap = hasTrap(nearTiles.x, nearTiles.y)
    val isPortal = false
    val isTransition = hasTransition(nearTiles.x, nearTiles.y)
    val lightValueType = lightValueType(lightInfo, position, positionDown, isPitFloor, isRoomFloorUp, isWall, isDoorUp)

    val wallTileSet = roomWallTileSet
    val floorBorderTileSet = floorBorderTileSet

    val emptyTile = arrayOf<TextureRegion>()

    val regions =
        if (noLightning) textureRegions.regions else
            when (lightValueType) {
                LightValueType.Full -> textureRegions.lightRegions
                LightValueType.Half -> textureRegions.regions
                else -> textureRegions.darkRegions
            }

    return when (layerType) {
        LayerType.Walls -> when {
            isWall -> arrayOf(indexedTextureTile(wallTileSet, wallTileIndex, regions))
            isRoomFloor || isCorridorFloor -> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, regions))
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isPitFloor -> arrayOf(regions[0][if (isPitFloorUp) 2 else 1])
            isRoomFloor || isCorridorFloor -> arrayOf(regions[(1..2).random()][(1..3).random()])
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            isTransition -> arrayOf(regions[4][10])
            isDoor -> arrayOf(regions[4][8])
            isTrap -> trapsTR(regions)
            isPortal -> portalTR(regions)
            else -> when {
                isWall && !isNextToDoor -> when {
                    isRoomWall -> arrayOf(regions[(0..1).random()][(8..11).random()])
                        .withChance(0.6f, defaultValue = emptyTile)
                    isCorridorWall -> arrayOf(regions[2][(8..11).random()])
                        .withChance(0.6f, defaultValue = emptyTile)
                    else -> emptyTile
                }
                else -> emptyTile
            }
        }
        else -> emptyTile
    }
}
