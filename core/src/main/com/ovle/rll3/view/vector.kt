package com.ovle.rll3.view

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.point
import ktx.math.vec2

fun Vector2.viewportToGame(): GridPoint2 = point(x / tileWidth, y / tileHeight)

fun GridPoint2.gameToViewport(): Vector2 = vec2(
    x * tileWidth.toFloat(),
    y * tileHeight.toFloat()
)


fun Vector2.centered() = vec2(x - tileWidth / 2.0f, y - tileHeight / 2.0f)
fun Vector2.uncentered() = vec2(x + tileWidth / 2.0f, y + tileHeight / 2.0f)
