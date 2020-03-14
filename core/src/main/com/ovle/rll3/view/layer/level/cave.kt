package com.ovle.rll3.view.layer.level

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.has
import com.ovle.rll3.model.ecs.entity.entitiesOnPosition
import com.ovle.rll3.model.ecs.entity.hasEntityOnPosition
import com.ovle.rll3.model.procedural.grid.floorTypes
import com.ovle.rll3.model.tile.LightValueType
import com.ovle.rll3.model.tile.pitFloorTileId
import com.ovle.rll3.model.tile.roomFloorTileId
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.point
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.layer.indoorFloorBorderTileSet
import com.ovle.rll3.view.layer.lightWallBorderTileSet
import com.ovle.rll3.view.layer.outdoorDarkFloorBorderTileSet
import com.ovle.rll3.view.noLightning
import ktx.ashley.get

data class TileTextureInfo(
    val regions: Array<TextureRegion>,
    val animationInterval: Float = defaultAnimationInterval
)

fun caveTileToTexture(params: TileToTextureParams): TileTextureInfo {
    val (layerType, nearTiles, textureRegions, levelInfo, lightInfo) = params

    val position = point(nearTiles.x, nearTiles.y)
    val positionDown = point(nearTiles.x, nearTiles.y - 1)

    val entities = entitiesOnPosition(levelInfo, position)
    fun hasLevelConnection(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), LevelConnectionComponent::class)
    fun hasWall(tileId: Int?) = if (tileId == null || (tileId == wallTileId)) 1 else 0
    fun hasPit(tileId: Int?) = if (tileId == pitFloorTileId) 1 else 0

    val upTileId = nearTiles.upValue?.typeId
    val downTileId = nearTiles.downValue?.typeId
    val rightTileId = nearTiles.rightValue?.typeId
    val leftTileId = nearTiles.leftValue?.typeId

    val wallBorderTileIndex = nearTiles.run {
        hasWall(rightTileId) + 2 * hasWall(downTileId) + 4 * hasWall(leftTileId) + 8 * hasWall(upTileId)
    }
    val tilesInSet = indoorFloorBorderTileSet.size * indoorFloorBorderTileSet.size - 1
    val floorBorderTileIndex = tilesInSet - nearTiles.run {
        hasPit(rightTileId) + 2 * hasPit(downTileId) + 4 * hasPit(leftTileId) + 8 * hasPit(upTileId)
    }
    val pitFloorBorderTileIndex = 0 + 1 * (hasPit(nearTiles.leftDownValue?.typeId)) + 2 * hasPit(nearTiles.rightDownValue?.typeId)

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isNextToFloor = upTileId in floorTypes
    val isRoomFloor = tileId == roomFloorTileId
    val isPitFloor = tileId == pitFloorTileId
    val isRoomFloorUp = downTileId == roomFloorTileId
    val isPitFloorUp = downTileId == pitFloorTileId

    val isLevelConnection = hasLevelConnection(nearTiles.x, nearTiles.y)
    val lightValueType = lightValueType(lightInfo, position, positionDown, isPitFloor, isRoomFloorUp, isWall, false)

    val wallBorderTileSet = lightWallBorderTileSet
    val floorBorderTileSet = outdoorDarkFloorBorderTileSet

    val emptyTile = arrayOf<TextureRegion>()

    val regions =
        if (noLightning) textureRegions.regions else
            when (lightValueType) {
                LightValueType.Full -> textureRegions.lightRegions
                LightValueType.Half -> textureRegions.regions
                else -> textureRegions.darkRegions
            }

    var animationInterval = defaultAnimationInterval
    val tileRegions =  when (layerType) {
        LayerType.Walls -> when {
            isWall -> arrayOf(indexedTextureTile(wallBorderTileSet, wallBorderTileIndex, regions))
            isRoomFloor -> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, regions))
            isPitFloor -> if (isPitFloorUp) emptyTile else arrayOf(regions[12][pitFloorBorderTileIndex])
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isWall && isNextToFloor -> arrayOf(regions[4][(4..4).random()])
            isRoomFloor -> arrayOf(regions[(4..5).random()][(0..3).random()])
            isPitFloor -> if (isPitFloorUp) emptyTile else arrayOf(regions[6][(0..2).random()])
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            isLevelConnection -> {
                val connectionComponent = entities.find { it.has<LevelConnectionComponent>() }!![levelConnection]!!
                val type = connectionComponent.type
                when {
                    type == LevelConnectionType.Up -> arrayOf(regions[5][9])
                    type == LevelConnectionType.Down -> arrayOf(regions[5][10])
                    else -> throw IllegalStateException("bad connection : type = $type")
                }
            }
            isRoomFloor -> arrayOf(regions[6][(4..9).random()])
                .withChance(0.3f, defaultValue = emptyTile)
            else -> emptyTile
        }
        else -> emptyTile
    }

    return TileTextureInfo(tileRegions, animationInterval)
}
