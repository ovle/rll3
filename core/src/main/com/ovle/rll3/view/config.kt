package com.ovle.rll3.view

import com.ovle.rll3.view.Palette.blackColor

const val initialScale = 1.4f
const val scaleScrollCoeff = 0.1f

const val screenWidth = 480.0f
const val screenHeight = 480.0f
const val tileWidth = 24
const val tileHeight = 24
const val textureTileWidth = tileWidth
const val textureTileHeight = tileHeight

const val spriteWidth = 24.0f
const val spriteHeight = 24.0f
const val gameFullscreen = false
const val skinPath = "skins/c64/uiskin.json"

const val tileTexturePath = "images/tiles.png"
const val spriteTexturePath = "images/sprites.png"
const val guiTexturePath = "images/gui.png"

const val entityTemplatePath = "templates/entity/"
const val structureTemplatePath = "templates/structure/"

val bgColor = blackColor.cpy()

const val defaultAnimationInterval = 0.125f

//debug
const val noLightning = true
const val noVisibilityFilter = false