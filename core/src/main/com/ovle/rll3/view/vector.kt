package com.ovle.rll3.view

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.point
import ktx.math.vec2
import ktx.math.vec3

fun Vector2.viewportToGame(): GridPoint2 = point(x / tileWidth, y / tileHeight)

fun GridPoint2.gameToViewport(): Vector2 = floatPoint(
    x * tileWidth.toFloat(),
    y * tileHeight.toFloat()
)

fun Vector2.screenToViewport(): Vector2 = RenderConfig.unproject(vec3(this)).let { vec2(it.x, it.y) }
fun Vector2.centered() = floatPoint(x - tileWidth / 2.0f, y - tileHeight / 2.0f)
fun Vector2.uncentered() = floatPoint(x + tileWidth / 2.0f, y + tileHeight / 2.0f)
