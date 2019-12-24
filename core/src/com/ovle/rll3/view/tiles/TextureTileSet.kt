package com.ovle.rll3.view.tiles

data class TextureTileSet(
    val id: String,
    val originX: Int = 0,
    val originY: Int = 0,
    val size: Int = 4
)

val roomWallTexTileSet = TextureTileSet("roomWall", originY = 4)
val passageWallTexTileSet = TextureTileSet("passageWall", originX = 4, originY = 4)
val roomFloorBorderTexTileSet = TextureTileSet("roomFloor", originX = 4)