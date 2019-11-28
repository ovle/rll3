package com.ovle.rll3.view.tiles

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.model.tile.NearTiles.Companion.nearTiles
import com.ovle.rll3.textureTileHeight
import com.ovle.rll3.textureTileWidth
import com.ovle.rll3.tileHeight
import com.ovle.rll3.tileWidth

class TextureTileSet(val id: String, val originX: Int = 0, val originY: Int = 0, val size: Int = 4)
val roomWallTexTileSet = TextureTileSet("roomWall", originY = 4)
val passageWallTexTileSet = TextureTileSet("passageWall", originX = 4, originY = 4)
val roomFloorTexTileSet = TextureTileSet("roomFloor", originX = 4)


enum class LayerType {
    Decoration,
    Walls,
    Floor,
    Bottom,
}

fun testLayer(tilesInfo: TilesInfo, texture: Texture, layerType: LayerType): MapLayer {
    val tiles = tilesInfo.tiles
    val result = TiledMapTileLayer(tiles.width, tiles.height, tileWidth, tileHeight)
    // todo cache / memo
    val textureRegions = TextureRegion.split(texture, textureTileWidth, textureTileHeight)

    for (x in 0 until tiles.width) {
        for (y in 0 until tiles.height) {
            val nearTiles = nearTiles(tiles, x, y)

            val resultTiles = textureTiles(layerType, nearTiles, textureRegions, tilesInfo)
            val cell = cellFromTiles(resultTiles)
            result.setCell(x, y, cell)
        }
        println()
    }

    return result
}

typealias TextureRegions = kotlin.Array<kotlin.Array<TextureRegion>>

private fun textureTiles(layerType: LayerType, nearTiles: NearTiles, textureRegions: TextureRegions, tilesInfo: TilesInfo): kotlin.Array<TextureRegion> {
    fun hasDoor(x: Int, y: Int) = tilesInfo.doorsInfo()?.any { it.x == x && it.y == y } ?: false
    fun hasWall(i: Int, x: Int, y: Int) = if (i == wallTileId || hasDoor(x, y)) 1 else 0
    fun hasRoomWall(i: Int) = if (i != roomFloorTileId) 1 else 0

    val wallTileIndex = nearTiles.run {
        hasWall(rightTileId, x+1, y) + 2 * hasWall(downTileId, x, y+1) + 4 * hasWall(leftTileId, x-1, y) + 8 * hasWall(upTileId, x, y-1)
    }
    val tilesInSet = roomFloorTexTileSet.size * roomFloorTexTileSet.size - 1
    val floorTileIndex = tilesInSet - nearTiles.run {
        hasRoomWall(rightTileId) + 2 * hasRoomWall(downTileId) + 4 * hasRoomWall(leftTileId) + 8 * hasRoomWall(upTileId)
    }

    val isRoomWall = nearTiles.upTileId == roomFloorTileId
    val wallTileSet = if (isRoomWall) roomWallTexTileSet else passageWallTexTileSet
    val floorTileSet = roomFloorTexTileSet

    val tileId = nearTiles.tileId
    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == roomFloorTileId
    val isCorridorFloor = tileId == corridorFloorTileId
//    val isRoomFloorNearHorisontal = nearTiles.nearHorisontal.contains(roomFloorTileId)
    val isRoomFloorNearVertical = nearTiles.nearVertical.contains(roomFloorTileId)
    val isDoor = hasDoor(nearTiles.x, nearTiles.y)

    //todo
    val resultTiles =
            when(layerType) {
                LayerType.Walls -> when {
                    isWall -> arrayOf(indexedTextureTile(wallTileSet, wallTileIndex, textureRegions))
                    isRoomFloor -> arrayOf(indexedTextureTile(floorTileSet, floorTileIndex, textureRegions))
                    isDoor -> arrayOf(textureRegions[3][(if (isRoomFloorNearVertical) 0 else 1)])
                    else -> arrayOf(emptyTextureRegion(textureRegions))
                }
                LayerType.Floor -> when {
                    isCorridorFloor -> arrayOf(textureRegions[0][(1..3).random()])
                    isRoomFloor -> arrayOf(textureRegions[(1..2).random()][(1..3).random()])
                    else -> arrayOf(emptyTextureRegion(textureRegions))
                }
                LayerType.Decoration -> when {
                    isWall && isRoomWall -> arrayOf(textureRegions[(0..1).random()][(8..11).random()]
                        .withChance(0.25f, defaultValue = emptyTextureRegion(textureRegions)))
                    else -> arrayOf(emptyTextureRegion(textureRegions))
                }
                else -> arrayOf(emptyTextureRegion(textureRegions))
            }
    return resultTiles
}

private fun emptyTextureRegion(textureRegions: TextureRegions) = textureRegions[0][0]

private fun TextureRegion.withChance(chance: Float, defaultValue: TextureRegion) = if (Math.random() <= chance) this else defaultValue

private fun indexedTextureTile(tileSetConfig: TextureTileSet, index: Int, textureRegions: TextureRegions) =
        textureRegions[tileSetConfig.originX + index % tileSetConfig.size][tileSetConfig.originY + index / tileSetConfig.size]

private fun cellFromTiles(resultTiles: kotlin.Array<TextureRegion>): TiledMapTileLayer.Cell {
    val staticTiles = resultTiles.map { StaticTiledMapTile(it) }.toTypedArray()
    val cell = TiledMapTileLayer.Cell()
    cell.tile = AnimatedTiledMapTile(0.25f, Array(staticTiles))
    return cell
}
