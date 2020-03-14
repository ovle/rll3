package com.ovle.rll3.view.layer

data class TextureTileSet(
    val id: String,
    val originX: Int = 0,
    val originY: Int = 0,
    val size: Int = 4
)

val darkWallBorderTileSet = TextureTileSet("dungeonWall", originX = 12, originY = 4)
val lightWallBorderTileSet = TextureTileSet("dungeonWall", originX = 12, originY = 8)
val indoorFloorBorderTileSet = TextureTileSet("indoorFloorBorder", originX = 16)
val outdoorDarkFloorBorderTileSet = TextureTileSet("outdoorFloorBorder", originX = 16, originY = 4)
val outdoorLightFloorBorderTileSet = TextureTileSet("outdoorFloorBorder", originX = 16, originY = 8)