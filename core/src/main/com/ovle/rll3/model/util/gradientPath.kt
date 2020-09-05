package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.util.GradientDirection.*
import com.ovle.rll3.point
import java.util.*

enum class GradientDirection {
    Up {
        override fun isEndPointReached(value: Float, finishValue: Float) = value >= finishValue
        override fun isNextPoint(value: Float, extremumValue: Float) = value > extremumValue
        override fun erosionCheck(value: Float, checkValue: Float, erosionPower: Float) = checkValue < value - erosionPower
        override fun extremumValue() = Float.MIN_VALUE
    },
    Down {
        override fun isEndPointReached(value: Float, finishValue: Float) = value <= finishValue
        override fun isNextPoint(value: Float, extremumValue: Float) = value < extremumValue
        override fun erosionCheck(value: Float, checkValue: Float, erosionPower: Float) = checkValue > value + erosionPower
        override fun extremumValue() = Float.MAX_VALUE
    };

    abstract fun isEndPointReached(value: Float, finishValue: Float): Boolean
    abstract fun isNextPoint(value: Float, extremumValue: Float): Boolean
    abstract fun erosionCheck(value: Float, checkValue: Float, erosionPower: Float): Boolean
    abstract fun extremumValue(): Float
}

data class GradientPathParams(
    val direction : GradientDirection = Down,
    val finishValue: Float = 0.3f,
    val erosionPower: Float = 0.01f,
    val maxRecursionDepth: Int = 1,
    val branchChance: Float = 0.1f,
    val random: Random
)

fun gradientPath(grid: Grid, from: GridPoint2, params: GradientPathParams): Area? {
    val result = Area()
    gradientPath(grid, result, from, params, 0)
    return result
}

private fun gradientPath(grid: Grid, path: Area, from: GridPoint2, params: GradientPathParams, recursionDepth: Int) {
    if (recursionDepth > params.maxRecursionDepth) return

    var previousPoint = from
    var newPoint: GridPoint2?
    var isEndPointReached = false

    while (!isEndPointReached) {
        newPoint = nextPoint(grid, path, previousPoint, params)
        if (newPoint == null) break

        if (params.random.nextFloat() <= params.branchChance) {
            gradientPath(grid, path, newPoint, params, recursionDepth + 1)
        }

        val value = grid[newPoint.x, newPoint.y]
        isEndPointReached = params.direction.isEndPointReached(value, params.finishValue)

        if (!isEndPointReached) {
            path.points.add(newPoint)
            previousPoint = newPoint
        }
    }
}

private fun nextPoint(grid: Grid, path: Area, previousPoint: GridPoint2, params: GradientPathParams): GridPoint2? {
    val x: Int = previousPoint.x
    val y: Int = previousPoint.y
    val direction = params.direction
    var extremumValue = direction.extremumValue()
    var nextX = -1
    var nextY = -1
    var didFindPoint = false

    for (i in x - 1..x + 1) {
        for (j in y - 1..y + 1) {
            /*if ((i != x) && (j != y)) { continue; }*/
            if (!grid.isIndexValid(i, j)) continue
            if (direction.erosionCheck(grid[x, y], grid[i, j], params.erosionPower)) continue
            if (point(i, j) in path.points) continue

            val isNewExtremum = direction.isNextPoint(grid[i, j], extremumValue)
            if (isNewExtremum) {
                extremumValue = grid[i, j]
                nextX = i
                nextY = j
                didFindPoint = true
            }
        }
    }
    return if (didFindPoint) point(nextX, nextY) else null
}
