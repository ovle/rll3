package com.ovle.rll3.view.layer.level

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.advanced.DoorComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent.LevelConnectionType.Down
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.has
import com.ovle.rll3.model.ecs.entity.entitiesOnPosition
import com.ovle.rll3.model.ecs.entity.hasEntityOnPosition
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.point
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.layer.indoorFloorBorderTileSet
import com.ovle.rll3.view.layer.lightWallBorderTileSet
import com.ovle.rll3.view.layer.outdoorDarkFloorBorderTileSet
import com.ovle.rll3.view.noLightning
import ktx.ashley.get

fun villageTileToTexture(params: TileToTextureParams): TileTextureInfo {
    val (layerType, nearTiles, textureRegions, levelInfo, lightInfo) = params

    val position = point(nearTiles.x, nearTiles.y)
    val positionDown = point(nearTiles.x, nearTiles.y - 1)

    fun hasDoor(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), DoorComponent::class)
    fun hasWall(tileId: TileType?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId) || (tileId == structureWallTileId) || hasDoor(x, y)) 1 else 0
    fun hasPit(tileId: TileType?) = if (tileId in pitTypes) 1 else 0
    fun hasNotInnerFloor(tileId: TileType?) = if (tileId != structureInnerFloorTileId) 1 else 0

    val upTileId = nearTiles.upValue?.typeId
    val downTileId = nearTiles.downValue?.typeId
    val rightTileId = nearTiles.rightValue?.typeId
    val leftTileId = nearTiles.leftValue?.typeId

    val wallBorderTileIndex = nearTiles.run {
        hasWall(rightTileId, x + 1, y) + 2 * hasWall(downTileId, x, y + 1) + 4 * hasWall(leftTileId, x - 1, y) + 8 * hasWall(upTileId, x, y - 1)
    }
    val tilesInSet = indoorFloorBorderTileSet.size * indoorFloorBorderTileSet.size - 1
    val floorBorderTileIndex = tilesInSet - nearTiles.run {
        hasPit(rightTileId) + 2 * hasPit(downTileId) + 4 * hasPit(leftTileId) + 8 * hasPit(upTileId)
    }
    val floorInnerBorderTileIndex = tilesInSet - nearTiles.run {
        hasNotInnerFloor(rightTileId) + 2 * hasNotInnerFloor(downTileId) + 4 * hasNotInnerFloor(leftTileId) + 8 * hasNotInnerFloor(upTileId)
    }

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isNextToDoor = hasDoor(nearTiles.x, nearTiles.y - 1)
    val isNextToFloor = upTileId in floorTypes && !isNextToDoor
    val isRightToFloor = rightTileId in floorTypes
    val isLeftToFloor = leftTileId in floorTypes

    val isNextToStructureFloor = upTileId in setOf(structureFloorTileId, structureInnerFloorTileId) && !isNextToDoor
    val isFloor = tileId == roomFloorTileId
    val isWaterFloor = tileId == waterTileId
    val isFloorUp = downTileId == roomFloorTileId

    val isInnerStructureFloor = tileId == structureInnerFloorTileId
    val isStructureFloor = isInnerStructureFloor || tileId == structureFloorTileId
    val isStructureWall = tileId == structureWallTileId
    val isRoad = tileId == roadTileId
    val isFence = tileId == fenceTileId
    val isHFence = isFence && nearTiles.nearH.any {  it?.typeId in wallTypes }
    val isVFence = isFence && nearTiles.nearV.any {  it?.typeId in wallTypes }
    val isCrossFence = isHFence && isVFence

    val isDoor = hasDoor(nearTiles.x, nearTiles.y)
    val lightValueType = lightValueType(lightInfo, position, positionDown, isWaterFloor, isFloorUp, isWall, false)

    val wallBorderTileSet = lightWallBorderTileSet
    val floorBorderTileSet = outdoorDarkFloorBorderTileSet
    val innerFloorBorderTileSet = indoorFloorBorderTileSet

    val emptyTile = arrayOf<TextureRegion>()

    val regions =
        if (noLightning) textureRegions.regions else
            when (lightValueType) {
                LightValueType.Full -> textureRegions.lightRegions
                LightValueType.Half -> textureRegions.regions
                else -> textureRegions.darkRegions
            }

    var animationInterval = defaultAnimationInterval * 2
    val tileRegions =  when (layerType) {
        LayerType.Walls -> when {
            isWall || isStructureWall || isDoor -> arrayOf(indexedTextureTile(wallBorderTileSet, wallBorderTileIndex, regions))
            isInnerStructureFloor -> arrayOf(indexedTextureTile(innerFloorBorderTileSet, floorInnerBorderTileIndex, regions))
            isFloor || isStructureFloor || isRoad -> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, regions))
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isDoor -> emptyTile
            isRoad -> arrayOf(regions[8][1])
            isCrossFence -> arrayOf(regions[9][7])
            isHFence -> arrayOf(regions[9][8])
            isVFence -> arrayOf(regions[9][9])
            isStructureWall && isNextToStructureFloor -> arrayOf(regions[8][5])
            isStructureWall && isNextToFloor -> arrayOf(regions[8][4])
            isInnerStructureFloor -> arrayOf(regions[2][8])
            isStructureFloor -> arrayOf(regions[8][(2..3).random()])
            isWall && isNextToFloor -> arrayOf(regions[9][(5..5).random()])
            isFloor -> arrayOf(regions[(9..10).random()][(0..3).random()])
            isWaterFloor -> arrayOf(
                regions[11][0],
                regions[11][1],
                regions[11][2]
            )
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            isStructureWall && isNextToFloor && !isNextToStructureFloor -> when {
                isRightToFloor -> arrayOf(regions[8][6])
                isLeftToFloor -> arrayOf(regions[8][7])
                else -> emptyTile
            }
            else -> emptyTile
        }
        else -> emptyTile
    }

    return TileTextureInfo(tileRegions, animationInterval)
}
