package com.ovle.rll3.model.procedural.grid.processor.world

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rlUtil.GridPointCheck2
import com.ovle.rll3.model.procedural.config.location.shallowWaterTreshold
import com.ovle.rll3.model.procedural.config.world.shallowWaterTileId
import com.ovle.rll3.model.procedural.grid.WorldProcessor
import com.ovle.rlUtil.gdx.math.Area
import com.ovle.rlUtil.noise4j.grid.GradientPathParams
import com.ovle.rlUtil.noise4j.grid.gradientPath
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rlUtil.noise4j.grid.apply

data class RiverInfo(
    val area: Area
)

data class RiverWorldProcessorParams(
    val count: Int = 10,
    val minLength: Int = 20,
    val finishHeightValue: Float = shallowWaterTreshold,
    val erosionPower: Float = 0.01f,
    val isStartPoint: GridPointCheck2
)

class RiverWorldProcessor(val params: RiverWorldProcessorParams): WorldProcessor {

    override fun process(worldInfo: WorldInfo) {
        val rivers = mutableListOf<RiverInfo>()
        val grid = worldInfo.heightGrid
        val random = worldInfo.random.jRandom
        val startPoints = getStartPoints(grid, worldInfo)

        val maxAttempts = 50
        var attempts = 0
        var count = 0
        while (count < params.count) {
            if (attempts > maxAttempts) break
            if (startPoints.isEmpty()) break

            val index: Int = random.nextInt(startPoints.size)
            val startPoint = startPoints[index]
            startPoints.remove(startPoint)

            val path = gradientPath(
                grid,
                startPoint,
                GradientPathParams(
                    random = random,
                    finishValue = params.finishHeightValue,
                    erosionPower = params.erosionPower
                )
            )
            val isNeedAddRiver = path != null && path.points.size > params.minLength

            if (isNeedAddRiver) {
                val riverInfo = RiverInfo(path!!)
                rivers.add(riverInfo)

                startPoints.removeAll { it in path.points }
                count++
            }

            attempts++
        }

        startPoints.clear()

//        println("rivers: ${rivers.size}")
        rivers.forEach {
            it.area.apply(worldInfo.tiles, shallowWaterTileId)
        }
    }

    private fun getStartPoints(grid: Grid, worldInfo: WorldInfo): MutableList<GridPoint2> {
        val result = mutableListOf<GridPoint2>()
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                val p = point(i, j)
                if (params.isStartPoint.invoke(grid, worldInfo.heatGrid, p)) {
                    result.add(p)
                }
            }
        }
//        println("start points (rivers): ${result.size}")
        return result
    }
}