package com.ovle.rll3

import com.badlogic.gdx.graphics.Color

const val screenWidth = 1024.0f
const val screenHeight = 768.0f
const val tileWidth = 24
const val tileHeight = 24
const val scale = 2.0f
const val tileMapScale = scale
const val spriteWidth = 24.0f
const val spriteHeight = 24.0f
const val spriteScale = scale
const val gameFullscreen = false
const val skinPath = "skins/c64/uiskin.json"
const val tileTexturePath = "images/tiles.png"
const val spriteTexturePath = "images/test-char.png"

val blackColor = Color(0x140c1cff)
val bgColor = blackColor.cpy()

//val darkBlueColor = Color(0x494D7E)
//val grayColor = Color(0x8B6D9C)
//val redColor = Color(0xC69FA5)
//val yellowColor = Color(0xF2D3AB)
//val whiteColor = Color(0xFBF5EF)

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
const val mapSizeInTiles = 16