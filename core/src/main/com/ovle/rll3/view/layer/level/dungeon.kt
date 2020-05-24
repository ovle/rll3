package com.ovle.rll3.view.layer.level

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.advanced.DoorComponent
import com.ovle.rll3.model.ecs.entity.anyOn
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.point
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.layer.indoorFloorBorderTileSet
import com.ovle.rll3.view.layer.lightWallBorderTileSet
import com.ovle.rll3.view.noLightning


fun dungeonTileToTexture(params: TileToTextureParams): TileTextureInfo {
    val (layerType, nearTiles, textureRegions, levelInfo, lightInfo) = params

    val position = point(nearTiles.x, nearTiles.y)
    val positionDown = point(nearTiles.x, nearTiles.y - 1)

    fun hasDoor(x: Int, y: Int): Boolean = levelInfo.entities.anyOn(point(x, y), DoorComponent::class)
    fun hasWall(tileId: TileType?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId || hasDoor(x, y))) 1 else 0
    fun hasWallOrPit(tileId: TileType?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId || tileId == pitFloorTileId /*|| hasDoor(x, y)*/)) 1 else 0
    fun hasPit(tileId: TileType?) = if (tileId == pitFloorTileId) 1 else 0

    val upTileId = nearTiles.upValue?.typeId
    val downTileId = nearTiles.downValue?.typeId
    val rightTileId = nearTiles.rightValue?.typeId
    val leftTileId = nearTiles.leftValue?.typeId

    val wallBorderTileIndex = nearTiles.run {
        hasWall(rightTileId, x + 1, y) + 2 * hasWall(downTileId, x, y + 1) + 4 * hasWall(leftTileId, x - 1, y) + 8 * hasWall(upTileId, x, y - 1)
    }
    val tilesInSet = indoorFloorBorderTileSet.size * indoorFloorBorderTileSet.size - 1
    val floorBorderTileIndex = tilesInSet - nearTiles.run {
        hasWallOrPit(rightTileId, x + 1, y) + 2 * hasWallOrPit(downTileId, x, y + 1) + 4 * hasWallOrPit(leftTileId, x - 1, y) + 8 * hasWallOrPit(upTileId, x, y - 1)
    }
    val pitFloorBorderTileIndex = 0 + 1 * (hasPit(nearTiles.leftDownValue?.typeId)) + 2 * hasPit(nearTiles.rightDownValue?.typeId)

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == groundTileId
    val isPitFloor = tileId == pitFloorTileId
    val isCorridorFloor = tileId == corridorTileId
    val isDoor = hasDoor(nearTiles.x, nearTiles.y)
    val isRoomFloorUp = downTileId == groundTileId
    val isPitFloorUp = downTileId == pitFloorTileId
    val isNextToDoor = hasDoor(nearTiles.x, nearTiles.y - 1)
    val isNextToFloor = upTileId in floorTypes && !isNextToDoor
    val isWallOrDoor = isWall || isDoor

    val lightValueType = lightValueType(lightInfo, position, positionDown, isPitFloor, isRoomFloorUp, isWall, isNextToDoor)

    val wallBorderTileSet = lightWallBorderTileSet
    val floorBorderTileSet = indoorFloorBorderTileSet

    val emptyTile = arrayOf<TextureRegion>()

    val regions =
        if (noLightning) textureRegions.regions else
            when (lightValueType) {
                LightValueType.Full -> textureRegions.lightRegions
                LightValueType.Half -> textureRegions.regions
                else -> textureRegions.darkRegions
            }

    var animationInterval = defaultAnimationInterval
    val tileRegions = when (layerType) {
        LayerType.Walls -> when {
            isWallOrDoor -> arrayOf(indexedTextureTile(wallBorderTileSet, wallBorderTileIndex, regions))
            isRoomFloor || isCorridorFloor -> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, regions))
            isPitFloor -> if (isPitFloorUp) emptyTile else arrayOf(regions[13][pitFloorBorderTileIndex])
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isDoor -> emptyTile
            isWall && isNextToFloor -> arrayOf(regions[(1..2).random()][(4..7).random()])
                .withChance(0.6f, defaultValue = arrayOf(regions[1][4]))
            isPitFloor -> arrayOf(regions[3][if (isPitFloorUp) 1 else 0])
            isRoomFloor || isCorridorFloor -> arrayOf(regions[(1..2).random()][(1..3).random()])
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            else -> emptyTile
        }
        else -> emptyTile
    }

    return TileTextureInfo(tileRegions, animationInterval)
}
