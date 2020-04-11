package com.ovle.rll3.view.layer.level

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent.LevelConnectionType.Down
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.has
import com.ovle.rll3.model.ecs.entity.entitiesOnPosition
import com.ovle.rll3.model.ecs.entity.hasEntityOnPosition
import com.ovle.rll3.model.procedural.grid.floorTypes
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

    val entities = entitiesOnPosition(levelInfo, position)
    fun hasLevelConnection(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), LevelConnectionComponent::class)
    fun hasWall(tileId: TileType?) = if (tileId == null || (tileId == wallTileId) || (tileId == structureWallTileId)) 1 else 0
    fun hasPit(tileId: TileType?) = if (tileId == pitFloorTileId) 1 else 0

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

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isNextToFloor = upTileId in floorTypes
    val isRightToFloor = rightTileId in floorTypes
    val isLeftToFloor = leftTileId in floorTypes

    val isNextToStructureFloor = upTileId == structureFloorTileId
    val isFloor = tileId == roomFloorTileId
    val isPitFloor = tileId == pitFloorTileId
    val isFloorUp = downTileId == roomFloorTileId
    val isPitFloorUp = downTileId == pitFloorTileId

    val isStructureFloor = tileId == structureFloorTileId
    val isStructureWall = tileId == structureWallTileId

    val isLevelConnection = hasLevelConnection(nearTiles.x, nearTiles.y)
    val lightValueType = lightValueType(lightInfo, position, positionDown, isPitFloor, isFloorUp, isWall, false)

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

    var animationInterval = defaultAnimationInterval * 2
    val tileRegions =  when (layerType) {
        LayerType.Walls -> when {
            isWall || isStructureWall -> arrayOf(indexedTextureTile(wallBorderTileSet, wallBorderTileIndex, regions))
            isFloor || isStructureFloor-> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, regions))
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isStructureWall && isNextToStructureFloor -> arrayOf(regions[8][5])
            isStructureWall && isNextToFloor -> arrayOf(regions[8][4])
            isStructureFloor -> arrayOf(regions[8][(2..3).random()])

            isWall && isNextToFloor -> arrayOf(regions[9][(4..5).random()])
            isFloor -> arrayOf(regions[(9..10).random()][(0..3).random()])
            isPitFloor -> arrayOf(
                regions[11][0],
                regions[11][1],
                regions[11][2]
            )
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            isLevelConnection -> {
                val connectionComponent = entities.find { it.has<LevelConnectionComponent>() }!![levelConnection]!!
                val type = connectionComponent.type
                when {
                    type == Down -> arrayOf(regions[7][9])
                    else -> throw IllegalStateException("bad connection : type = $type")
                }
            }
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
