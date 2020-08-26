package com.ovle.rll3.model.procedural.grid.generator

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.grid.util.normalize
import kotlin.math.abs

class GradientMapGenerator {

    companion object {
        private const val DEFAULT_BOUNDS_SHARPNESS = 3
        private const val DEFAULT_DRY_VALUE_KOEFF = 10
    }

    private val startValue = 0.0f
    private val endValue = 1.0f
    var isMirrored = true
    var boundsSharpness = DEFAULT_BOUNDS_SHARPNESS
    var dryValueCoeff = DEFAULT_DRY_VALUE_KOEFF //TODO: rename
    val isHorisontal = false

    fun generate(grid: Grid) {
        processArea(grid, startValue, endValue)
//        makeNonUniformBounds(result)
        normalize(grid)
    }

    private fun processArea(grid: Grid, startValue: Float, endValue: Float) {
        val width = grid.width
        val height = grid.height
        var averageStep: Float = abs(endValue - startValue) / height
        if (isMirrored) {
            averageStep *= 2f
        }

        for (i in 0 until width) {
            for (j in 0 until height) {
                grid.setValue(i, j, averageStep * j)
                if (isMirrored) {
                    grid.setValue(i, height - j - 1, averageStep * j)
                    val isHalf = j >= height / 2
                    if (isHalf) {
                        break
                    }
                }
            }
        }
    }

    private fun Grid.setValue(i: Int, j: Int, value: Float) {
        when {
            isHorisontal -> this[i, j] = value
            else -> this[j, i] = value
        }
    }

//    private fun makeNonUniformBounds(areaTiles: Array<FloatArray>) {
//        val height = areaTiles.size
//        val width: Int = areaTiles[0].size
//        val fractalMapGenerator = FractalMapGenerator()
//        fractalMapGenerator.startIteration = boundsSharpness
//        fractalMapGenerator.generateMap(GridPoint2(width, height))
//        val map: Array<FloatArray> = fractalMapGenerator.getMap()
//        val midValue = (endValue - startValue) / 2
//        //for every row
//        for (i in 0 until height) {
//            //for every column
//            for (j in 0 until width) {
//                val noiseValue = (midValue - map[j][i]) / dryValueCoeff
//                areaTiles[j][i] += noiseValue
//                if (areaTiles[j][i] < startValue) {
//                    areaTiles[j][i] = startValue
//                } else if (areaTiles[j][i] > endValue) {
//                    areaTiles[j][i] = endValue
//                }
//            }
//        }
//    }
}
