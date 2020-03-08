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
import com.ovle.rll3.model.procedural.grid.corridorFloorTypes
import com.ovle.rll3.model.procedural.grid.roomFloorTypes
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.point
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.layer.dungeonWallTileSet
import com.ovle.rll3.view.layer.indoorFloorBorderTileSet
import com.ovle.rll3.view.noLightning
import ktx.ashley.get


fun dungeonTileToTexture(params: TileToTextureParams): TileTextureInfo {
    val (layerType, nearTiles, textureRegions, levelInfo, lightInfo) = params

    val position = point(nearTiles.x, nearTiles.y)
    val positionDown = point(nearTiles.x, nearTiles.y - 1)

    val entities = entitiesOnPosition(levelInfo, position)
    fun hasDoor(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), DoorComponent::class)
    fun hasTrap(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), TriggerComponent::class)
    fun hasLevelConnection(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), LevelConnectionComponent::class)
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
    val tilesInSet = indoorFloorBorderTileSet.size * indoorFloorBorderTileSet.size - 1
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
    val isLevelConnection = hasLevelConnection(nearTiles.x, nearTiles.y)
    val lightValueType = lightValueType(lightInfo, position, positionDown, isPitFloor, isRoomFloorUp, isWall, isDoorUp)

    val wallTileSet = dungeonWallTileSet
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
            else -> when {
                isWall && !isNextToDoor -> when {
                    isRoomWall -> arrayOf(regions[(0..1).random()][(8..11).random()])
                        .withChance(0.6f, defaultValue = emptyTile)
//                    isCorridorWall -> arrayOf(regions[2][(8..11).random()])
//                        .withChance(0.6f, defaultValue = emptyTile)
                    else -> emptyTile
                }
                else -> emptyTile
            }
        }
        else -> emptyTile
    }

    return TileTextureInfo(tileRegions, animationInterval)
}
