package com.ovle.rll3.view.tiles

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.tileHeight
import com.ovle.rll3.tileWidth
import com.ovle.rll3.view.tiles.NearTiles.Companion.nearTiles
import com.ovle.rll3.withChance

class TextureTileSet(val id: String, val originX: Int = 0, val originY: Int = 0, val size: Int = 4)
val roomWallTexTileSet = TextureTileSet("roomWall", originY = 4)
val passageWallTexTileSet = TextureTileSet("passageWall", originX = 4, originY = 4)
val roomFloorTexTileSet = TextureTileSet("roomFloor", originX = 4)

const val wallTileId = 1
const val roomFloorTileId = 0
const val corridorFloorTileId = 2
const val outOfMapTileId = 1



fun hasWall(i: Int) = if (i == 1) 1 else 0
fun hasFloor(i: Int) = if (i != 0) 1 else 0

enum class LayerType {
    Decoration,
    Walls,
    Floor,
    Bottom,
}

fun testLayer(tiles: TileArray, texture: Texture, layerType: LayerType): MapLayer {
    val result = TiledMapTileLayer(tiles.width, tiles.height, tileWidth, tileHeight)
    // todo cache / memo
    val textureRegions = TextureRegion.split(texture, tileWidth, tileHeight)

    for (x in 0 until tiles.width) {
        for (y in 0 until tiles.height) {
            val nearTiles = nearTiles(tiles, x, y)

            val resultTiles = textureTiles(layerType, nearTiles, textureRegions)
            val cell = cellFromTiles(resultTiles)
            result.setCell(x, y, cell)
        }
        println()
    }

    return result
}

typealias TextureRegions = kotlin.Array<kotlin.Array<TextureRegion>>

private fun textureTiles(layerType: LayerType, nearTiles: NearTiles, textureRegions: TextureRegions): kotlin.Array<TextureRegion> {
    val wallTileIndex = nearTiles.run {
        hasWall(rightTileId) + 2 * hasWall(downTileId) + 4 * hasWall(leftTileId) + 8 * hasWall(upTileId)
    }
    val floorTileIndex = 15 - nearTiles.run {
        hasFloor(rightTileId) + 2 * hasFloor(downTileId) + 4 * hasFloor(leftTileId) + 8 * hasFloor(upTileId)
    }

//    val upTiles = arrayOf(nearTiles.upTileId)
    val isRoomWall = nearTiles.upTileId == roomFloorTileId //upTiles.count { it == corridorFloorTileId } <= upTiles.count { it == roomFloorTileId }
    val wallTileSet = if (isRoomWall) roomWallTexTileSet else passageWallTexTileSet
    val floorTileSet = roomFloorTexTileSet

    val tileId = nearTiles.tileId
    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == roomFloorTileId
    val isCorridorFloor = tileId == corridorFloorTileId
    //todo doors is matter of logic, not rendering only
    val isRoomFloorNearHorisontal = nearTiles.nearHorisontal.contains(roomFloorTileId)
    val isRoomFloorNearVertical = nearTiles.nearVertical.contains(roomFloorTileId)
    val isDoor = isCorridorFloor && (isRoomFloorNearHorisontal || isRoomFloorNearVertical)

    //todo
    val resultTiles =
            when(layerType) {
                LayerType.Walls -> when {
                    isWall -> arrayOf(indexedTextureTile(wallTileSet, wallTileIndex, textureRegions))
                    isRoomFloor -> arrayOf(indexedTextureTile(floorTileSet, floorTileIndex, textureRegions))
                    isDoor -> arrayOf(textureRegions[3][(if (isRoomFloorNearVertical) 0 else 1)])
                    else -> arrayOf(textureRegions[0][0])
                }
                LayerType.Floor -> when {
                    isCorridorFloor -> arrayOf(textureRegions[0][(1..3).random()])
                    isRoomFloor -> arrayOf(textureRegions[(1..2).random()][(1..3).random()])
                    else -> arrayOf(textureRegions[0][0])
                }
                LayerType.Decoration -> when {
                    isWall && isRoomWall -> arrayOf(textureRegions[0][(8..11).random().withChance(0.2f)])
                    else -> arrayOf(textureRegions[0][0])
                }
                else -> arrayOf(textureRegions[0][0])
            }
    return resultTiles
}

private fun indexedTextureTile(tileSetConfig: TextureTileSet, index: Int, textureRegions: TextureRegions) =
        textureRegions[tileSetConfig.originX + index % tileSetConfig.size][tileSetConfig.originY + index / tileSetConfig.size]

private fun cellFromTiles(resultTiles: kotlin.Array<TextureRegion>): TiledMapTileLayer.Cell {
    val staticTiles = resultTiles.map { StaticTiledMapTile(it) }.toTypedArray()
    val cell = TiledMapTileLayer.Cell()
    cell.tile = AnimatedTiledMapTile(0.25f, Array(staticTiles))
    return cell
}
