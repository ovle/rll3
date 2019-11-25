package com.ovle.rll3

import com.badlogic.gdx.graphics.Color

const val initialScale = 1.0f
const val scaleScrollCoeff = 0.1f

const val screenWidth = 1024.0f
const val screenHeight = 768.0f
const val tileWidth = 24
const val tileHeight = 24
const val textureTileWidth = tileWidth
const val textureTileHeight = tileHeight

const val spriteWidth = 24.0f
const val spriteHeight = 24.0f
const val gameFullscreen = false
const val skinPath = "skins/c64/uiskin.json"
const val tileTexturePath = "images/tiles.png"
const val spriteTexturePath = "images/test-char.png"

val blackColor = Color(0x272744ff)
val bgColor = blackColor.cpy()

val testRoomTemplate = arrayOf(
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 1, 1, 1, 1, 0, 0),
        arrayOf(0, 1, 1, 1, 1, 1, 1, 0),
        arrayOf(0, 1, 1, -1, -1, 1, 1, 0),
        arrayOf(0, 1, 1, -1, -1, 1, 1, 0),
        arrayOf(0, 1, 1, 1, 1, 1, 1, 0),
        arrayOf(0, 0, 1, 1, 1, 1, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0)
)
const val mapSizeInTiles = 33

////todo global data
//object RenderConfig {
//    var scale: Float = initialScale
//    var scrollOffset: Vector2 = Vector2()
//}
