package com.ovle.rll3.view

const val initialScale = 1.3f
const val scaleScrollCoeff = 0.05f
const val cameraMoveCoeff = 0.5f

const val screenWidth = 480.0f
const val screenHeight = 480.0f

const val baseSize = 8

const val tileWidth = baseSize
const val tileHeight = baseSize
const val textureTileWidth = tileWidth
const val textureTileHeight = tileHeight
const val spriteWidth = baseSize.toFloat()
const val spriteHeight = baseSize.toFloat()

const val gameFullscreen = false
const val skinPath = "skins/c64/uiskin.json"

const val tileTexturePath = "images/new-tiles.png"
const val spriteTexturePath = "images/new-tiles.png"
const val guiTexturePath = "images/new-tiles.png"

const val entityTemplatePath = "templates/entity/"
const val entityViewTemplatePath = "templates/entity/view/"
const val structureTemplatePath = "templates/structure/"
const val behaviorTreePath = "templates/behaviorTree/"

const val defaultAnimationInterval = 0.125f

//debug
var noLightning = true
var noVisibilityFilter = true