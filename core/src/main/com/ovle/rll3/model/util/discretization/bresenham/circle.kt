package com.ovle.rll3.model.util.discretization.bresenham

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.point


fun circle(center: GridPoint2, radius: Int): List<GridPoint2> {
    val result = mutableListOf<GridPoint2>()
    val cX = center.x
    val cY = center.y
    var d = ((5 - radius * 4) / 4.0f)
    var x = 0
    var y = radius

    do {
        //fill cross-wise
        result.addAll(
            arrayOf(
                point(cX + x, cY + y),
                point(cX + x, cY - y),
                point(cX - x, cY + y),
                point(cX - x, cY - y),

                point(cX + y, cY + x),
                point(cX + y, cY - x),
                point(cX - y, cY + x),
                point(cX - y, cY - x)
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

fun filledCircle(center: GridPoint2, radius: Int): List<GridPoint2> = circle(center, radius).distinct().fill()

//todo o(n2), use queue instead
private fun List<GridPoint2>.fill(): List<GridPoint2> {
    val minX = this.minBy { it.x }!!.x
    val maxX = this.maxBy { it.x }!!.x
    val minYs = (minX..maxX).associateWith { x -> this.filter { it.x == x }.minBy { it.y }!!.y }
    val maxYs = (minX..maxX).associateWith { x -> this.filter { it.x == x }.maxBy { it.y }!!.y }

    return (minX..maxX).flatMap {
        x ->
        val minY = minYs.getValue(x)
        val maxY = maxYs.getValue(x)
        (minY..maxY).map {
            y -> point(x, y)
        }
    }
}
