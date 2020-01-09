package com.ovle.rll3.model.util.discretization.bresenham

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.TilePosition
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.lineOfSight.rayTracing.trace
import kotlin.math.roundToInt


fun circle(center: Vector2, radius: Int): List<TilePosition> {
    val result = mutableListOf<TilePosition>()
    val centerX = center.x.roundToInt()
    val centerY = center.y.roundToInt()
    var d = (5 - radius * 4) / 4
    var x = 0
    var y = radius

    do {
        //fill cross-wise
        result.addAll(
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

    return result
}


fun filledCircle(center: Vector2, radius: Int, passMapper: (Tile) -> LightPassType, tiles: TileArray): List<TilePosition> {
    val result = circle(center, radius)
    val roundedCenter = center.x.roundToInt() to center.y.roundToInt()
    return result.flatMap {
        trace(roundedCenter, it, passMapper, tiles)
    }
}