package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.config.GridFactoryParams.GradientGridFactoryParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.procedural.grid.util.normalize
import kotlin.math.abs


class GradientGridFactory(val params: GradientGridFactoryParams): GridFactory {

    override fun get(random: RandomParams): Grid {
        val result = Grid(params.size)
        processArea(result)
//        makeNonUniformBounds(result)
        normalize(result)

        return result
    }

    private fun processArea(grid: Grid) {
        val width = grid.width
        val height = grid.height
        var averageStep: Float = abs(params.endValue - params.startValue) / height
        val isMirrored = params.isMirrored

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
            params.isHorisontal -> this[i, j] = value
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