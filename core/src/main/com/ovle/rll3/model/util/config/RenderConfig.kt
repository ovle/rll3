package com.ovle.rll3.model.util.config

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.ovle.rll3.view.initialScale
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth


object RenderConfig {
    var scale: Float = initialScale //todo fix scale <> 1
    var scrollOffset: Vector2 = Vector2(screenWidth / 2, screenHeight / 2)
    var unproject: ((Vector3) -> Vector3)? = null
}