package com.ovle.rll3.model.tile

data class NearTiles(
    val x: Int, val y: Int,
    val tileId: Int,
    val rightTileId: Int, val downTileId: Int, val leftTileId: Int, val upTileId: Int,
    val rightUpTileId: Int, val rightDownTileId: Int, val leftUpTileId: Int, val leftDownTileId: Int
) {
    companion object {
        fun nearTiles(tiles: TileArray, x: Int, y: Int): NearTiles {
            val tileId = tiles.get(y, x).typeId

            val upTileId = if (y > 0) tiles.get(y-1, x).typeId else outOfMapTileId
            val downTileId = if (y < tiles.height - 1) tiles.get(y+1, x).typeId else outOfMapTileId
            val leftTileId  = if (x > 0) tiles.get(y, x-1).typeId else outOfMapTileId
            val rightTileId  = if (x < tiles.width - 1) tiles.get(y, x+1).typeId else outOfMapTileId

            val rightUpTileId = if (y > 0 && x < tiles.width - 1) tiles.get(y-1, x+1).typeId else outOfMapTileId
            val rightDownTileId = if (y < tiles.height - 1 && x < tiles.width - 1) tiles.get(y+1, x+1).typeId else outOfMapTileId
            val leftUpTileId = if (y > 0 && x > 0) tiles.get(y-1, x-1).typeId else outOfMapTileId
            val leftDownTileId = if (y < tiles.height - 1 && x > 0) tiles.get(y+1, x-1).typeId else outOfMapTileId

            return NearTiles(x, y, tileId, rightTileId, downTileId, leftTileId, upTileId, rightUpTileId, rightDownTileId, leftUpTileId, leftDownTileId)
        }
    }

    val all = arrayOf(tileId, rightTileId, leftTileId, upTileId, downTileId, rightUpTileId, rightDownTileId, leftUpTileId, leftDownTileId)
    val allHV = arrayOf(tileId, rightTileId, leftTileId, upTileId, downTileId)
    val near = arrayOf(rightTileId, leftTileId, upTileId, downTileId, rightUpTileId, rightDownTileId, leftUpTileId, leftDownTileId)
    val nearHV = arrayOf(rightTileId, leftTileId, upTileId, downTileId)
    val nearH = arrayOf(rightTileId, leftTileId)
    val nearV = arrayOf(upTileId, downTileId)
}