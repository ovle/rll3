package com.ovle.rll3.view.layer

data class TextureTileSet(
    val id: String,
    val originX: Int = 0,
    val originY: Int = 0,
    val size: Int = 4
)

val dungeonWallTileSet = TextureTileSet("dungeonWall", originY = 4)
//val townWallTileSet = TextureTileSet("caveWall", originX = 8, originY = 4)
val caveWallTileSet = TextureTileSet("caveWall", originX = 4, originY = 4)
val indoorFloorBorderTileSet = TextureTileSet("indoorFloorBorder", originX = 16)
val outdoorFloorBorderTileSet = TextureTileSet("outdoorFloorBorder", originX = 16, originY = 4)