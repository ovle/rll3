package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.procedural.config.location.outdoorHighGroundTreshold
import com.ovle.rll3.model.procedural.config.location.shallowWaterTreshold
import com.ovle.rll3.model.procedural.config.world.heatUpperTreshold
import com.ovle.rll3.model.procedural.config.world.shallowWaterTileId
import com.ovle.rll3.model.procedural.config.world.worldShallowWaterTreshold
import com.ovle.rll3.model.procedural.grid.WorldProcessor
import com.ovle.rll3.model.procedural.grid.util.GradientPathParams
import com.ovle.rll3.model.procedural.grid.util.Path
import com.ovle.rll3.model.procedural.grid.util.gradientPath
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.point

data class RiverInfo(
    val path: Path
)

data class RiverWorldProcessorParams(
    val count: Int = 10,
    val minHeightValue: Float = outdoorHighGroundTreshold,
    val maxHeatValue: Float = heatUpperTreshold,
    val finishHeightValue: Float = shallowWaterTreshold,
    val minLength: Int = 20,
    val erosionValue: Float = 0.1f,
    val erosionPower: Float = 0.01f
)

class RiverWorldProcessor(val params: RiverWorldProcessorParams): WorldProcessor {

    private val startPoints = mutableListOf<GridPoint2>()
    private var rivers = mutableListOf<RiverInfo>()


    override fun process(worldInfo: WorldInfo) {
        startPoints.clear()
        rivers.clear()

        val grid = worldInfo.heightGrid
        val width = grid.width
        val height = grid.height
        val random = worldInfo.random.jRandom

        for (i in 0 until width) {
            for (j in 0 until height) {
                val p = point(i, j)
                if (isStartPoint(worldInfo, p)) {
                    startPoints.add(p)
                }
            }
        }
        println("start points: ${startPoints.size}")

        var pathsCount = 0
        while (pathsCount < params.count) {
            if (startPoints.size == 0) break

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
            } else {
                pathsCount--
            }

            pathsCount++
        }

        startPoints.clear()

        println("rivers: ${rivers.size}")
        rivers.forEach {
            applyRiver(worldInfo, it)
        }
    }

    private fun applyRiver(worldInfo: WorldInfo, river: RiverInfo) {
        val grid = worldInfo.heightGrid
        val tiles = worldInfo.tiles
        val erosionValue = params.erosionValue
        val path = river.path

        path.points.forEach {
            val x = it.x
            val y = it.y

            val gridValue = grid[x, y]
            if (gridValue > worldShallowWaterTreshold + erosionValue) {
                grid.set(x, y, gridValue - erosionValue)
            }

            tiles[x, y] = shallowWaterTileId
        }
    }

    private fun isStartPoint(worldInfo: WorldInfo, point: GridPoint2): Boolean {
        val heightValue = worldInfo.heightGrid[point.x, point.y]
        val heatValue = worldInfo.heatGrid[point.x, point.y]

        return heightValue > params.minHeightValue && heatValue < params.maxHeatValue
    }
}