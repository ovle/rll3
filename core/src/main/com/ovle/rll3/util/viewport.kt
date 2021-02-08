package com.ovle.rll3.util

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rll3.view.tileSize

fun Vector2.viewportToGame(): GridPoint2 = point(x / tileSize, y / tileSize)

fun GridPoint2.gameToViewport(): Vector2 = ktx.math.vec2(
    x * tileSize.toFloat(),
    y * tileSize.toFloat()
)
