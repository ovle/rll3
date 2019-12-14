package com.ovle.rll3.model.util.discretization.bresenham

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.TilePosition
import kotlin.math.roundToInt


fun circle(entityPosition: Vector2, radius: Int): List<TilePosition> {
    val visiblePositions = mutableListOf<TilePosition>()
    val centerX = entityPosition.x.roundToInt()
    val centerY = entityPosition.y.roundToInt()
    var d = (5 - radius * 4) / 4
    var x = 0
    var y = radius

    do {
        //fill cross-wise
        visiblePositions.addAll(
            arrayOf(
                centerX + x to centerY + y,
                centerX + x to centerY - y,
                centerX - x to centerY + y,
                centerX - x to centerY - y,
                centerX + y to centerY + x,
                centerX + y to centerY - x,
                centerX - y to centerY + x,
                centerX - y to centerY - x
            )
        )
        //rotate
        if (d < 0) {
            d += 2 * x + 1
        } else {
            d += 2 * (x - y) + 1
            y--
        }
        x++

    } while (x <= y)

    return visiblePositions
}