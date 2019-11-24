package com.ovle.rll3.view.tiles

import com.ovle.rll3.model.tile.TileArray

data class NearTiles(val tileId: Int, val rightTileId: Int, val downTileId: Int, val leftTileId: Int, val upTileId: Int) {
    companion object {
        fun nearTiles(tiles: TileArray, x: Int, y: Int): NearTiles {
            val tileId = tiles.get(y, x).typeId
            val upTileId = if (y > 0) tiles.get(y-1, x).typeId else outOfMapTileId
            val downTileId = if (y < tiles.height - 1) tiles.get(y+1, x).typeId else outOfMapTileId
            val leftTileId  = if (x > 0) tiles.get(y, x-1).typeId else outOfMapTileId
            val rightTileId  = if (x < tiles.width - 1) tiles.get(y, x+1).typeId else outOfMapTileId
            return NearTiles(tileId, rightTileId, downTileId, leftTileId, upTileId)
        }
    }

    val all = arrayOf(tileId, rightTileId, leftTileId, upTileId, downTileId)
    val near = arrayOf(rightTileId, leftTileId, upTileId, downTileId)
    val nearHorisontal = arrayOf(rightTileId, leftTileId)
    val nearVertical = arrayOf(upTileId, downTileId)
}