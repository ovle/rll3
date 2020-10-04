//package com.ovle.rll3.model.procedural.grid.processor.location
//
//import com.badlogic.ashley.core.Engine
//import com.badlogic.gdx.math.GridPoint2
//import com.github.czyzby.noise4j.map.Grid
//import com.ovle.rll3.component1
//import com.ovle.rll3.component2
//import com.ovle.rll3.model.module.game.LocationInfo
//import com.ovle.rll3.model.procedural.config.location.outdoorHighGroundTreshold
//import com.ovle.rll3.model.procedural.config.location.outdoorLowGroundTreshold
//import com.ovle.rll3.model.procedural.config.location.shallowWaterTileId
//import com.ovle.rll3.model.procedural.grid.LocationProcessor
//import com.ovle.rll3.model.util.*
//import com.ovle.rll3.point
//
//data class PondLocationProcessorParams(
//    val count: Int = 10,
//    val sizeRange: IntRange = (4..36),
//    val depth: Float = 0.005f
//)
//
//class PondLocationProcessor(val params: PondLocationProcessorParams): LocationProcessor {
//
//    override fun process(locationInfo: LocationInfo, gameEngine: Engine) {
//        val ponds = mutableListOf<Area>()
//        val grid = locationInfo.heightGrid
//        val random = locationInfo.random.jRandom
//        val startPoints = getStartPoints(grid, locationInfo)
//
//        val maxAttempts = 50
//        var attempts = 0
//        var count = 0
//        while (count < params.count) {
//            if (attempts > maxAttempts) break
//            if (startPoints.isEmpty()) break
//
//            val index: Int = random.nextInt(startPoints.size)
//            val startPoint = startPoints[index]
//            startPoints.remove(startPoint)
//            val (x, y) = startPoint
//            val finishHeightValue = grid[x, y] + params.depth
//            fun check(value: Float) = value < finishHeightValue
//            println("-------- start floodFill ($x $y) finishHeightValue: $finishHeightValue")
//
//            val area = floodFill(x, y, grid, ::check)
//
//            println("pond size = ${area.points.size}")
//            startPoints.removeAll { it in area.points }
//
//            val isNeedAddPond = true//area.points.size in params.sizeRange
//            if (isNeedAddPond) {
//                ponds.add(area)
//                count++
//            }
//
//            attempts++
//        }
//
//        startPoints.clear()
//
//        println("ponds: ${ponds.size}")
//        ponds.forEach {
//            it.apply(locationInfo.tiles, shallowWaterTileId)
//        }
//    }
//
//    private fun getStartPoints(grid: Grid, locationInfo: LocationInfo): MutableList<GridPoint2> {
//        val array = grid.array
//        val maxValue = array.max()!!
//        val minValue = array.min()!!
//        val tresholdValue = minValue + (maxValue - minValue) / 5.0f
//
//        val result = mutableListOf<GridPoint2>()
//        for (i in 0 until grid.width) {
//            for (j in 0 until grid.height) {
//                val value = grid[i, j]
//                if (value <= outdoorLowGroundTreshold) continue
//                if (value >= outdoorHighGroundTreshold) continue
//
//                if (value < tresholdValue) {
//                    result.add(point(i, j))
//                }
//            }
//        }
//        println("start points (ponds): ${result.size}")
//        return result
//    }
//}