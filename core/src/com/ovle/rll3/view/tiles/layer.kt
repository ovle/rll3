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
import kotlin.math.roundToInt

class TextureTileSet(val id: String, val originX: Int = 0, val originY: Int = 0, val size: Int = 4)
val roomWallTexTileSet = TextureTileSet("roomWall", originY = 4)
val passageWallTexTileSet = TextureTileSet("passageWall", originX = 4, originY = 4)
val roomFloorTexTileSet = TextureTileSet("roomFloor", originX = 4)

const val wallTileId = 1
const val roomFloorTileId = 0
const val corridorFloorTileId = 2
const val outOfMapTileId = 1

fun IntRange.random() = (Math.random() * (this.last - this.first)).roundToInt() + this.first
fun <T> List<T>.random(): T? = if (this.isEmpty()) null else this[(0 until size).random()]

fun emptyWall(i: Int) = if (i == 1) 1 else 0
fun emptyFloor(i: Int) = if (i != 0) 1 else 0

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
            val tileId = tiles.get(y, x).typeId
            val upTileId = if (y > 0) tiles.get(y-1, x).typeId else outOfMapTileId
            val downTileId = if (y < tiles.height - 1) tiles.get(y+1, x).typeId else outOfMapTileId
            val leftTileId  = if (x > 0) tiles.get(y, x-1).typeId else outOfMapTileId
            val rightTileId  = if (x < tiles.width - 1) tiles.get(y, x+1).typeId else outOfMapTileId

            val resultTiles = textureTiles(layerType, tileId, rightTileId, downTileId, leftTileId, upTileId, textureRegions)
            val cell = cellFromTiles(resultTiles)
            result.setCell(x, y, cell)
        }
        println()
    }

    return result
}

private fun textureTiles(layerType: LayerType, tileId: Int, rightTileId: Int, downTileId: Int, leftTileId: Int, upTileId: Int, textureRegions: kotlin.Array<kotlin.Array<TextureRegion>>): kotlin.Array<TextureRegion> {

    val wallTileIndex = emptyWall(rightTileId) + 2 * emptyWall(downTileId) + 4 * emptyWall(leftTileId) + 8 * emptyWall(upTileId)
    val floorTileIndex = 15 - (emptyFloor(rightTileId) + 2 * emptyFloor(downTileId) + 4 * emptyFloor(leftTileId) + 8 * emptyFloor(upTileId))

    val nearTiles = arrayOf(upTileId)
    val isRoomWall = nearTiles.count { it == 2 } <= nearTiles.count { it == 0 }
    val wallTileSet = if (isRoomWall) roomWallTexTileSet else passageWallTexTileSet
    val floorTileSet = roomFloorTexTileSet

    val isWall = tileId == wallTileId
    val isRoomFloor = tileId == roomFloorTileId
    val isCorridorFloor = tileId == corridorFloorTileId
    //todo doors is matter of logic, not rendering only
    val isRoomFloorNearHorisontal = arrayOf(leftTileId, rightTileId).contains(roomFloorTileId)
    val isRoomFloorNearVertical = arrayOf(upTileId, downTileId).contains(roomFloorTileId)
    val isDoor = isCorridorFloor && (isRoomFloorNearHorisontal || isRoomFloorNearVertical)

    //todo
    val resultTiles =
            when(layerType) {
                LayerType.Walls -> when {
                    isWall -> arrayOf(textureRegions[wallTileSet.originX + wallTileIndex % wallTileSet.size][wallTileSet.originY + wallTileIndex / wallTileSet.size])
                    isRoomFloor -> arrayOf(textureRegions[floorTileSet.originX + floorTileIndex % floorTileSet.size][floorTileSet.originY + floorTileIndex / floorTileSet.size])
                    isDoor -> arrayOf(textureRegions[3][(if (isRoomFloorNearVertical) 0 else 1)])
                    else -> arrayOf(textureRegions[0][0])
                }
                LayerType.Floor -> when {
                    isCorridorFloor -> arrayOf(textureRegions[0][(1..3).random()])
                    isRoomFloor -> arrayOf(textureRegions[(1..2).random()][(1..3).random()])
                    else -> arrayOf(textureRegions[0][0])
                }
                else -> arrayOf(textureRegions[0][0])
            }
    return resultTiles
}

private fun cellFromTiles(resultTiles: kotlin.Array<TextureRegion>): TiledMapTileLayer.Cell {
    val staticTiles = resultTiles.map { StaticTiledMapTile(it) }.toTypedArray()
    val cell = TiledMapTileLayer.Cell()
    cell.tile = AnimatedTiledMapTile(0.25f, Array(staticTiles))
    return cell
}
