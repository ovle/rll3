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
fun IntRange.random() = (Math.random() * (this.last - this.first)).roundToInt() + this.first
fun <T> List<T>.random(): T? = if (this.isEmpty()) null else this[(0 until size).random()]
fun empty(i: Int) = if (i == 1) 1 else 0


fun testLayer(tiles: TileArray, texture: Texture): MapLayer {
    val result = TiledMapTileLayer(tiles.width, tiles.height, tileWidth, tileHeight)
    val textureTiles = TextureRegion.split(texture, tileWidth, tileHeight)
    val wallTileId = 1
    val outOfMapTileId = 1

    for (x in 0 until tiles.width) {
        for (y in 0 until tiles.height) {
            val tileId = tiles.get(y, x).typeId
            val upTileId = if (y > 0) tiles.get(y-1, x).typeId else outOfMapTileId
            val downTileId = if (y < tiles.height - 1) tiles.get(y+1, x).typeId else outOfMapTileId
            val leftTileId  = if (x > 0) tiles.get(y, x-1).typeId else outOfMapTileId
            val rightTileId  = if (x < tiles.width - 1) tiles.get(y, x+1).typeId else outOfMapTileId

            var tileIndex = empty(rightTileId) + 2 * empty(downTileId) + 4 * empty(leftTileId) + 8 * empty(upTileId)
            print("${ if (tileId == 1) tileIndex else 0}  ")
            //todo
            if (tileId == 0) tileIndex = 15 - tileIndex

            val nearTiles = arrayOf(upTileId)
//            val nearTiles = arrayOf(upTileId, downTileId, leftTileId, rightTileId)
            val isRoomWall = nearTiles.count { it == 2 } <= nearTiles.count { it == 0 }
            val wallTileSet = if (isRoomWall) roomWallTexTileSet else passageWallTexTileSet
            val floorTileSet = roomFloorTexTileSet
            val textureTiles = when (tileId) {
                wallTileId -> arrayOf(textureTiles[wallTileSet.originX + tileIndex % wallTileSet.size][wallTileSet.originY + tileIndex / wallTileSet.size])
//                2 -> arrayOf(textureTiles[0][(1..3).random()])
                0 -> arrayOf(textureTiles[floorTileSet.originX + tileIndex % floorTileSet.size][floorTileSet.originY + tileIndex / floorTileSet.size])
                else -> arrayOf(textureTiles[0][0])
            }

            val staticTiles = textureTiles.map { StaticTiledMapTile(it) }.toTypedArray()
            val cell = TiledMapTileLayer.Cell()
            cell.tile = AnimatedTiledMapTile(0.25f, Array(staticTiles))
            result.setCell(x, y, cell)
        }
        println()
    }

    return result
}
