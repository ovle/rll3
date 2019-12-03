package com.ovle.rll3.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

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

const val defaultAnimationInterval = 0.125f

//todo make entity/component?
data class RenderConfig(
    var scale: Float = initialScale,
    var scrollOffset: Vector2 = Vector2(screenWidth / 2, screenHeight / 2),
    var unproject: ((Vector3) -> Vector3)? = null
)

val renderConfig = RenderConfig()