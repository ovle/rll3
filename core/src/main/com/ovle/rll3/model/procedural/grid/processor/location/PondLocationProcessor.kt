package com.ovle.rll3.model.procedural.grid.processor.location

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.config.location.outdoorHighGroundTreshold
import com.ovle.rll3.model.procedural.config.location.outdoorLowGroundTreshold
import com.ovle.rll3.model.procedural.config.location.shallowWaterTileId
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import com.ovle.rll3.model.util.Area
import com.ovle.rll3.model.util.GradientDirection
import com.ovle.rll3.model.util.GradientPathParams
import com.ovle.rll3.model.util.gradientPath
import com.ovle.rll3.point

data class PondLocationProcessorParams(
    val count: Int = 10,
    val sizeRange: IntRange = (4..36)
)

class PondLocationProcessor(val params: PondLocationProcessorParams): LocationProcessor {

    override fun process(locationInfo: LocationInfo, gameEngine: Engine) {
        val ponds = mutableListOf<Area>()
        val grid = locationInfo.heightGrid
        val random = locationInfo.random.jRandom
        val startPoints = getStartPoints(grid, locationInfo)

        var count = 0
        while (count < params.count) {
            if (startPoints.isEmpty()) break

            val index: Int = random.nextInt(startPoints.size)
            val startPoint = startPoints[index]
            startPoints.remove(startPoint)
            val finishHeightValue = grid[startPoint.x, startPoint.y] + 0.1f

            val path = gradientPath(
                grid,
                startPoint,
                GradientPathParams(
                    random = random,
                    finishValue = finishHeightValue,
                    erosionPower = 0.0f,
                    direction = GradientDirection.Up
                )
            )
            val isNeedAddPond = path != null && path.points.size in params.sizeRange
            if (isNeedAddPond) {
                ponds.add(path!!)

                startPoints.removeAll { it in path.points }
            } else {
                count--
            }

            count++
        }

        startPoints.clear()

        println("ponds: ${ponds.size}")
        ponds.forEach {
            it.apply(locationInfo.tiles, shallowWaterTileId)
        }
    }

    private fun getStartPoints(grid: Grid, locationInfo: LocationInfo): MutableList<GridPoint2> {
        val array = grid.array
        val maxValue = array.max()!!
        val minValue = array.min()!!
        val tresholdValue = minValue + (maxValue - minValue) / 5.0f

        val result = mutableListOf<GridPoint2>()
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                val value = grid[i, j]
                if (value <= outdoorLowGroundTreshold) continue
                if (value >= outdoorHighGroundTreshold) continue

                if (value < tresholdValue) {
                    result.add(point(i, j))
                }
            }
        }
        println("start points: ${result.size}")
        return result
    }
}