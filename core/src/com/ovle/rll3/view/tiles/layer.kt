package com.ovle.rll3.view.tiles

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.procedural.corridorFloorTypes
import com.ovle.rll3.model.procedural.roomFloorTypes
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.view.*
import java.lang.Math.random

class TextureTileSet(val id: String, val originX: Int = 0, val originY: Int = 0, val size: Int = 4)
val roomWallTexTileSet = TextureTileSet("roomWall", originY = 4)
val passageWallTexTileSet = TextureTileSet("passageWall", originX = 4, originY = 4)
val roomFloorBorderTexTileSet = TextureTileSet("roomFloor", originX = 4)


enum class LayerType {
    Decoration,
    Walls,
    Floor,
    Bottom,
}


fun testLayer(levelInfo: LevelInfo, texture: Texture, layerType: LayerType): MapLayer {
    val tiles = levelInfo.tiles
    val result = CustomTiledMapTileLayer(tiles.width, tiles.height, tileWidth, tileHeight)
    // todo cache / memo

    //todo
//    val pm = texture.textureData.consumePixmap()
//    for (x in 0..pm.width)
//    for (y in 0..pm.height) {
//        val p = pm.getPixel(x, y)
//        if ()
//        pm.drawPixel()
//    }

    val textureRegions = TextureRegion.split(texture, textureTileWidth, textureTileHeight)

    for (x in 0 until tiles.width) {
        for (y in 0 until tiles.height) {
            val nearTiles = nearValues(tiles, x, y)

            val tileTextureRegions = tileTextureRegions(layerType, nearTiles, textureRegions, levelInfo)
            val cell = cellFromTileTextureRegions(tileTextureRegions)
            result.setCell(x, y, cell)
        }
        println()
    }

    return result
}

typealias TextureRegions = kotlin.Array<kotlin.Array<TextureRegion>>

private fun tileTextureRegions(layerType: LayerType, nearTiles: NearTiles, textureRegions: TextureRegions, levelInfo: LevelInfo): kotlin.Array<TextureRegion> {

    fun hasDoor(x: Int, y: Int, levelInfo: LevelInfo): Boolean = levelInfo.hasEntityOnPosition(x, y, DoorComponent::class)
    fun hasLight(x: Int, y: Int, levelInfo: LevelInfo): Boolean = levelInfo.hasEntityOnPosition(x, y, LightComponent::class)
    fun hasWall(tileId: Int?, x: Int, y: Int) = if (tileId == null || (tileId == wallTileId || hasDoor(x, y, levelInfo))) 1 else 0
    fun hasRoomWall(tileId: Int?): Int = if (tileId != null && tileId != roomFloorTileId) 1 else 0

    val upTileId = nearTiles.upValue?.typeId
    val downTileId = nearTiles.downValue?.typeId
    val rightTileId = nearTiles.rightValue?.typeId
    val leftTileId = nearTiles.leftValue?.typeId

    val wallTileIndex = nearTiles.run {
        hasWall(rightTileId, x+1, y) + 2 * hasWall(downTileId, x, y+1) + 4 * hasWall(leftTileId, x-1, y) + 8 * hasWall(upTileId, x, y-1)
    }
    val tilesInSet = roomFloorBorderTexTileSet.size * roomFloorBorderTexTileSet.size - 1
    val floorBorderTileIndex = tilesInSet - nearTiles.run {
        hasRoomWall(rightTileId) + 2 * hasRoomWall(downTileId) + 4 * hasRoomWall(leftTileId) + 8 * hasRoomWall(upTileId)
    }

    val tileId = nearTiles.value?.typeId
    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == roomFloorTileId
    val isPitFloor = tileId == pitFloorTileId
    val isCorridorFloor = tileId == corridorFloorTileId
    val isDoor = hasDoor(nearTiles.x, nearTiles.y, levelInfo)
    val isNextToDoor = hasDoor(nearTiles.x, nearTiles.y-1, levelInfo)

    val isRoomWall = upTileId in roomFloorTypes
    val isCorridorWall = upTileId in corridorFloorTypes
    val isPitFloorUp = downTileId == pitFloorTileId
    val isRoomFloorNearVertical = roomFloorTileId in nearTiles.nearV.map { it?.typeId }

    val isLightSource = hasLight(nearTiles.x, nearTiles.y, levelInfo)
    val isTrap = isRoomFloor && random() > 0.85f
    val isPortal = isRoomFloor && random() > 0.95f
    val wallTileSet = if (isRoomWall) roomWallTexTileSet else passageWallTexTileSet
    val floorBorderTileSet = roomFloorBorderTexTileSet
    val emptyTile  = arrayOf<TextureRegion>()

    return when(layerType) {
        LayerType.Walls -> when {
            isWall -> arrayOf(indexedTextureTile(wallTileSet, wallTileIndex, textureRegions))
            isRoomFloor -> arrayOf(indexedTextureTile(floorBorderTileSet, floorBorderTileIndex, textureRegions))
            isDoor -> arrayOf(textureRegions[4][(if (isRoomFloorNearVertical) 8 else 9)])
            else -> emptyTile
        }
        LayerType.Floor -> when {
            isPitFloor -> arrayOf(textureRegions[3][if (isPitFloorUp) 1 else 0])
            isCorridorFloor -> arrayOf(textureRegions[0][(1..3).random()])
            isRoomFloor -> arrayOf(textureRegions[(1..2).random()][(1..3).random()])
            else -> emptyTile
        }
        LayerType.Decoration -> when {
            //todo
            isLightSource -> arrayOf(
                textureRegions[8][8],
                textureRegions[8][9],
                textureRegions[8][10],
                textureRegions[8][11]
            )
            isTrap -> arrayOf(
                textureRegions[7][8],
                textureRegions[7][9],
                textureRegions[7][10],
                textureRegions[7][11]
            )
            isPortal -> arrayOf(
                textureRegions[9][8],
                textureRegions[9][9],
                textureRegions[9][10],
                textureRegions[9][11],
                textureRegions[10][8],
                textureRegions[10][9],
                textureRegions[10][10],
                textureRegions[10][11]
            )
            else -> when {
                isWall && !isNextToDoor -> when {
                    isRoomWall -> arrayOf(textureRegions[(0..1).random()][(8..11).random()]).withChance(0.6f, defaultValue = emptyTile)
                    isCorridorWall -> arrayOf(textureRegions[2][(8..11).random()]).withChance(0.6f, defaultValue = emptyTile)
                    else -> emptyTile
                }
                else -> emptyTile
            }
        }
        else -> emptyTile
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
