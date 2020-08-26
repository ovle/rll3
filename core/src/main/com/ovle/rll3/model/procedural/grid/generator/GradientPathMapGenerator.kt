package com.ovle.rll3.model.procedural.grid.generator

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.point
import java.util.*


class GradientPathMapGenerator(
    var random: Random
) {

    private data class Path(val points: MutableSet<GridPoint2> = mutableSetOf())

    var maxPaths = 10

    var minStartValue = 0.8f
    var maxFinishValue = 0.3f
    var pathValue = 0.3f

    var maxErosionPower = 3
    var maxRecursionDepth = 1
    var minPathLength = 20

    private val startPoints = mutableListOf<GridPoint2>()
    private var paths = mutableListOf<Path>()

    fun generate(grid: Grid) {
        val width = grid.width
        val height = grid.height

        for (i in 0 until width) {
            for (j in 0 until height) {
                val p = point(i, j)
                if (isStartPoint(grid, p)) {
                    startPoints.add(p)
                }
            }
        }

        println("start points: ${startPoints.size}")

        var pathsCount = 0
        while (pathsCount < maxPaths) {
            if (startPoints.size == 0) {
                break
            }
            val index: Int = random.nextInt(startPoints.size)
            val startPoint = startPoints[index]
            startPoints.remove(startPoint)

            val path = generatePath(grid, startPoint.x, startPoint.y)

            if (path != null && path.points.size > minPathLength) {
                paths.add(path)
            } else {
                pathsCount--
            }
            pathsCount++
        }

        startPoints.clear()

        println("paths: ${paths.size}")

        paths.forEach {
            applyPath(grid, it, pathValue)
        }
    }

    private fun applyPath(grid: Grid, path: Path, pathValue: Float) {
        path.points.forEach {
            grid.set(it.x, it.y, pathValue)
        }
    }

    private fun generatePath(grid: Grid, fromX: Int, fromY: Int): Path? {
        val result = Path()
        generatePath(grid, result, fromX, fromY, 0)
        return  result
    }

    private fun generatePath(grid: Grid, path: Path, fromX: Int, fromY: Int, recursionDepth: Int) {
        if (recursionDepth > maxRecursionDepth) return

        var previousPoint = GridPoint2(fromX, fromY)
        var newPoint: GridPoint2?
        var isEndPointReached = false

        while (!isEndPointReached) {
            newPoint = nextPoint(grid, path, previousPoint)
            if (newPoint == null) break

            if (random.nextInt(20) > 16) {  //todo why 20? 16?
                generatePath(grid, path, newPoint.x, newPoint.y, recursionDepth + 1)
            }

            isEndPointReached = isEndPoint(grid, newPoint)
            if (!isEndPointReached) {
                path.points.add(newPoint)

                startPoints.remove(newPoint)
                previousPoint = newPoint
            }
        }
    }

    private fun nextPoint(grid: Grid, path: Path, previousPoint: GridPoint2): GridPoint2? {
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
                if (grid[i, j] > grid[x, y] + maxErosionPower) continue
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

    private fun isStartPoint(grid: Grid, newPoint: GridPoint2) =
        grid[newPoint.x, newPoint.y] > minStartValue

    private fun isEndPoint(grid: Grid, newPoint: GridPoint2) =
        grid[newPoint.x, newPoint.y] <= maxFinishValue
}
