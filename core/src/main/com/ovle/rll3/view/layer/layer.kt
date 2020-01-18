package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.hasEntityOnPosition
import com.ovle.rll3.model.procedural.corridorFloorTypes
import com.ovle.rll3.model.procedural.roomFloorTypes
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.model.tile.LightValueType.*
import com.ovle.rll3.model.util.config.LightConfig
import com.ovle.rll3.view.defaultAnimationInterval
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth


enum class LayerType {
    Decoration,
    Walls,
    Floor,
    Bottom,
}

fun testLayer(levelInfo: LevelInfo, texturesInfo: TexturesInfo, layerType: LayerType): MapLayer {
    val tiles = levelInfo.tiles
    val result = CustomTiledMapTileLayer(tiles.width, tiles.height, tileWidth, tileHeight)

    val textureRegions = TextureRegionsInfo(texturesInfo)
    val lightTiles = lightTiles(levelInfo)
    val lightInfo = lightByPosition(lightTiles)

    for (x in 0 until tiles.width) {
        for (y in 0 until tiles.height) {
            val nearTiles = nearValues(tiles, x, y)
            val tileTextureRegions = tileTextureRegions(layerType, nearTiles, textureRegions, levelInfo, lightInfo)
            val cell = cellFromTileTextureRegions(tileTextureRegions)
            result.setCell(x, y, cell)
        }
    }

    return result
}


private fun tileTextureRegions(
    layerType: LayerType, nearTiles: NearTiles, textureRegions: TextureRegionsInfo, levelInfo: LevelInfo, lightInfo: Map<GridPoint2, Double>
): kotlin.Array<TextureRegion> {
    val position = point(nearTiles.x, nearTiles.y)
    val positionDown = point(nearTiles.x, nearTiles.y - 1)

    fun hasDoor(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), DoorComponent::class)
    fun hasLightSource(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), LightComponent::class)
    fun hasTrap(x: Int, y: Int): Boolean = hasEntityOnPosition(levelInfo, point(x, y), TrapComponent::class)
    fun hasWall(tileId: Int?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId || hasDoor(x, y))) 1 else 0
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
        hasRoomWall(rightTileId) + 2 * hasRoomWall(downTileId) + 4 * hasRoomWall(leftTileId) + 8 * hasRoomWall(upTileId)
    }

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == roomFloorTileId
    val isPitFloor = tileId == pitFloorTileId
    val isCorridorFloor = tileId == corridorFloorTileId
    val isDoor = hasDoor(nearTiles.x, nearTiles.y)
    val isNextToDoor = hasDoor(nearTiles.x, nearTiles.y-1)
    val isRoomWall = upTileId in roomFloorTypes
    val isCorridorWall = upTileId in corridorFloorTypes
    val isDoorUp = hasDoor(nearTiles.x, nearTiles.y - 1)
    val isRoomFloorUp = downTileId == roomFloorTileId
    val isPitFloorUp = downTileId == pitFloorTileId
    val isRoomFloorNearVertical = roomFloorTileId in nearTiles.nearV.map { it?.typeId }

    val isLightSource = hasLightSource(nearTiles.x, nearTiles.y)
    val isTrap = hasTrap(nearTiles.x, nearTiles.y)
    val lightValueType = lightValueType(lightInfo, position, positionDown, isPitFloor, isRoomFloorUp, isWall, isDoorUp)

    val wallTileSet = if (isRoomWall) roomWallTileSet else passageWallTileSet
    val floorBorderTileSet = floorBorderTileSet

    val emptyTile  = arrayOf<TextureRegion>()
    val regions = when(lightValueType) {
        Full -> textureRegions.lightRegions
        Half -> textureRegions.regions
        else -> textureRegions.darkRegions
    }

    return when(layerType) {
        LayerType.Walls -> when {
            isWall -> arrayOf(indexedTextureTile(wallTileSet, wallTileIndex, regions))
            isRoomFloor -> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, regions))
            isDoor -> arrayOf(regions[4][(if (isRoomFloorNearVertical) 8 else 9)])
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isPitFloor -> arrayOf(regions[3][if (isPitFloorUp) 1 else 0])
            isCorridorFloor -> arrayOf(regions[0][(1..3).random()])
            isRoomFloor -> arrayOf(regions[(1..2).random()][(1..3).random()])
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            //todo
            isLightSource -> arrayOf(
                regions[8][8],
                regions[8][9],
                regions[8][10],
                regions[8][11]
            )
            isTrap -> arrayOf(
                regions[7][8],
                regions[7][9],
                regions[7][10],
                regions[7][11]
            )
//            isPortal -> arrayOf(
//                textureRegions[9][8],
//                textureRegions[9][9],
//                textureRegions[9][10],
//                textureRegions[9][11],
//                textureRegions[10][8],
//                textureRegions[10][9],
//                textureRegions[10][10],
//                textureRegions[10][11]
//            )
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

private fun lightValueType(
    lightInfo: Map<GridPoint2, Double>, position: GridPoint2, positionDown: GridPoint2, isPitFloor: Boolean, isRoomFloorUp: Boolean, isWall: Boolean, isDoorUp: Boolean
): LightValueType {
    val tileLightValue = lightInfo[position] ?: 0.0
    val tileLightDownValue = lightInfo[positionDown] ?: 0.0

    val isFullLight = tileLightValue > LightConfig.halfLightCap
    val isHalfLight = !isFullLight && tileLightValue > LightConfig.darknessCap
    val isFullLightDown = tileLightDownValue > LightConfig.halfLightCap
    val isHalfLightDown = !isFullLightDown && tileLightDownValue > LightConfig.darknessCap

    return when {
        isFullLight -> when {
            !isPitFloor || isRoomFloorUp -> Full
            else -> No
        }
        isHalfLight -> when {
            !isPitFloor || isRoomFloorUp -> Half
            else -> No
        }
        else -> when {
            isWall && isFullLightDown && !isDoorUp -> Full
            isWall && isHalfLightDown && !isDoorUp -> Half
            else -> No
        }
    }
}

private fun kotlin.Array<TextureRegion>.withChance(chance: Float, defaultValue: kotlin.Array<TextureRegion>) = if (Math.random() <= chance) this else defaultValue

private fun indexedTextureTile(tileSetConfig: TextureTileSet, index: Int, textureRegions: TextureRegions) =
        textureRegions[tileSetConfig.originX + index % tileSetConfig.size][tileSetConfig.originY + index / tileSetConfig.size]

private fun cellFromTileTextureRegions(tileTextureRegions: kotlin.Array<TextureRegion>): TiledMapTileLayer.Cell {
    val cell = TiledMapTileLayer.Cell()
    if (tileTextureRegions.isNotEmpty()) {
        val staticTiles = tileTextureRegions.map { StaticTiledMapTile(it) }.toTypedArray()
        cell.tile = if (staticTiles.size == 1) staticTiles.single()
            else AnimatedTiledMapTile(defaultAnimationInterval, Array(staticTiles))
    }
    return cell
}