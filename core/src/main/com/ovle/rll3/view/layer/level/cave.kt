package com.ovle.rll3.view.layer.level

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.TriggerComponent
import com.ovle.rll3.model.ecs.component.has
import com.ovle.rll3.model.ecs.entity.entitiesOnPosition
import com.ovle.rll3.model.ecs.entity.hasEntityOnPosition
import com.ovle.rll3.model.tile.LightValueType
import com.ovle.rll3.model.tile.pitFloorTileId
import com.ovle.rll3.model.tile.roomFloorTileId
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.point
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.layer.caveWallTileSet
import com.ovle.rll3.view.layer.indoorFloorBorderTileSet
import com.ovle.rll3.view.layer.outdoorFloorBorderTileSet
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
    fun hasDoor(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), DoorComponent::class)
    fun hasTrap(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), TriggerComponent::class)
    fun hasLevelConnection(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), LevelConnectionComponent::class)
    fun hasWall(tileId: Int?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId || hasDoor(x, y))) 1 else 0
    fun hasPit(tileId: Int?, x: Int, y: Int) = if (tileId == pitFloorTileId) 1 else 0

    val upTileId = nearTiles.upValue?.typeId
    val downTileId = nearTiles.downValue?.typeId
    val rightTileId = nearTiles.rightValue?.typeId
    val leftTileId = nearTiles.leftValue?.typeId

    val wallTileIndex = nearTiles.run {
        hasWall(rightTileId, x + 1, y) + 2 * hasWall(downTileId, x, y + 1) + 4 * hasWall(leftTileId, x - 1, y) + 8 * hasWall(upTileId, x, y - 1)
    }
    val tilesInSet = indoorFloorBorderTileSet.size * indoorFloorBorderTileSet.size - 1
    val floorBorderTileIndex = tilesInSet - nearTiles.run {
        hasPit(rightTileId, x + 1, y) + 2 * hasPit(downTileId, x, y + 1) + 4 * hasPit(leftTileId, x - 1, y) + 8 * hasPit(upTileId, x, y - 1)
    }

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == roomFloorTileId
    val isPitFloor = tileId == pitFloorTileId
    val isDoor = hasDoor(nearTiles.x, nearTiles.y)
    val isDoorUp = hasDoor(nearTiles.x, nearTiles.y - 1)
    val isRoomFloorUp = downTileId == roomFloorTileId
    val isPitFloorUp = downTileId == pitFloorTileId

    val isTrap = hasTrap(nearTiles.x, nearTiles.y)
    val isPortal = false
    val isLevelConnection = hasLevelConnection(nearTiles.x, nearTiles.y)
    val lightValueType = lightValueType(lightInfo, position, positionDown, isPitFloor, isRoomFloorUp, isWall, isDoorUp)

    val wallTileSet = caveWallTileSet
    val floorBorderTileSet = outdoorFloorBorderTileSet

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
            isWall -> arrayOf(indexedTextureTile(wallTileSet, wallTileIndex, regions))
            isRoomFloor -> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, regions))
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isRoomFloor -> arrayOf(regions[4][(0..1).random()])
            isPitFloor -> if (isPitFloorUp) emptyTile else arrayOf(regions[5][(0..2).random()])
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            isLevelConnection -> {
                val connectionComponent = entities.find { it.has<LevelConnectionComponent>() }!![levelConnection]!!
                val type = connectionComponent.type
                when {
                    !connectionComponent.visited -> arrayOf(regions[3][10])
                    type == LevelConnectionType.Up -> arrayOf(regions[3][11])
                    type == LevelConnectionType.Down -> arrayOf(regions[2][11])
                    else -> throw IllegalStateException("bad connection : type = $type")
                }
            }
            //todo
            isDoor -> arrayOf(regions[3][8])
            isPitFloor -> {
                animationInterval = 0.25f
                arrayOf(
                    regions[15][0],
                    regions[15][1],
                    regions[15][2]
                )
            }
            isRoomFloor -> arrayOf(regions[(4..7).random()][(8..11).random()])
                .withChance(0.3f, defaultValue = emptyTile)
            else -> emptyTile
        }
        else -> emptyTile
    }

    return TileTextureInfo(tileRegions, animationInterval)
}
