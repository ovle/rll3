package com.ovle.rll3.view.layer

data class TextureTileSet(
    val id: String,
    val originX: Int = 0,
    val originY: Int = 0,
    val size: Int = 4
)

val roomWallTileSet = TextureTileSet("roomWall", originY = 4)
val caveWallTileSet = TextureTileSet("passageWall", originX = 4, originY = 4)
val floorBorderTileSet = TextureTileSet("roomFloor", originX = 4)