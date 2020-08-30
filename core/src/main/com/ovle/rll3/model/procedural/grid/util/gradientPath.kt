package com.ovle.rll3.model.procedural.grid.util

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.point
import java.util.*

data class Path(val points: MutableSet<GridPoint2> = mutableSetOf())

data class GradientPathParams(
    val finishValue: Float = 0.3f,
    val erosionPower: Float = 0.01f,
    val maxRecursionDepth: Int = 1,
    val branchChance: Float = 0.1f,
    val random: Random
)


fun gradientPath(grid: Grid, from: GridPoint2, params: GradientPathParams): Path? {
    val result = Path()
    gradientPath(grid, result, from, params, 0)
    return result
}

private fun gradientPath(grid: Grid, path: Path, from: GridPoint2, params: GradientPathParams, recursionDepth: Int) {
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

        isEndPointReached = grid[newPoint.x, newPoint.y] <= params.finishValue
        if (!isEndPointReached) {
            path.points.add(newPoint)

            previousPoint = newPoint
        }
    }
}

private fun nextPoint(grid: Grid, path: Path, previousPoint: GridPoint2, params: GradientPathParams): GridPoint2? {
    val x: Int = previousPoint.x
    val y: Int = previousPoint.y
    var minValue = Float.MAX_VALUE
    var nextX = -1
    var nextY = -1
    var didFindPoint = false
    for (i in x - 1..x + 1) {
        for (j in y - 1..y + 1) {
            //TODO: test!
            /*if ((i != x) && (j != y)) {
                continue;
            }*/
            if (!grid.isIndexValid(i, j)) continue
            if (grid[i, j] > grid[x, y] + params.erosionPower) continue
            if (point(i, j) in path.points) continue

            if (grid[i, j] < minValue) {
                minValue = grid[i, j]
                nextX = i
                nextY = j
                didFindPoint = true
            }
        }
    }
    return if (didFindPoint) point(nextX, nextY) else null
}
