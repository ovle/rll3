package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.procedural.config.shallowWaterTreshold
import com.ovle.rll3.model.procedural.grid.LevelProcessor
import com.ovle.rll3.model.procedural.grid.util.GradientPathParams
import com.ovle.rll3.model.procedural.grid.util.Path
import com.ovle.rll3.model.procedural.grid.util.gradientPath
import com.ovle.rll3.model.tile.shallowWaterTileId
import com.ovle.rll3.point

data class RiverInfo(
    val path: Path
)

data class RiverLevelProcessorParams(
    val count: Int = 10,
    val minStartValue: Float = 0.8f,
    val minLength: Int = 20
)

class RiverLevelProcessor(val params: RiverLevelProcessorParams): LevelProcessor {

    private val erosionValue = 0.1f

    private val startPoints = mutableListOf<GridPoint2>()
    private var rivers = mutableListOf<RiverInfo>()


    override fun process(levelInfo: LevelInfo, gameEngine: Engine) {
        val grid = levelInfo.sourceGrid
        val width = grid.width
        val height = grid.height
        val r = levelInfo.random.jRandom

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
        while (pathsCount < params.count) {
            if (startPoints.size == 0) {
                break
            }
            val index: Int = r.nextInt(startPoints.size)
            val startPoint = startPoints[index]
            startPoints.remove(startPoint)

            val path = gradientPath(grid, startPoint, GradientPathParams(r = r))
            val isNeedAddRiver = path != null && path.points.size > params.minLength

            if (isNeedAddRiver) {
                val riverInfo = RiverInfo(path!!)
                rivers.add(riverInfo)

                startPoints.removeAll { it in path.points }
            } else {
                pathsCount--
            }
            pathsCount++
        }

        startPoints.clear()

        println("rivers: ${rivers.size}")
        rivers.forEach {
            applyRiver(levelInfo, it)
        }
    }

    private fun applyRiver(levelInfo: LevelInfo, river: RiverInfo) {
        val grid = levelInfo.sourceGrid
        val tiles = levelInfo.tiles
        val path = river.path

        path.points.forEach {
            val x = it.x
            val y = it.y

            val gridValue = grid[x, y]
            if (gridValue > shallowWaterTreshold + erosionValue) {
                grid.set(x, y, gridValue - erosionValue)
            }

            tiles[x, y] = shallowWaterTileId
        }
    }

    private fun isStartPoint(grid: Grid, newPoint: GridPoint2) =
        grid[newPoint.x, newPoint.y] > params.minStartValue
}