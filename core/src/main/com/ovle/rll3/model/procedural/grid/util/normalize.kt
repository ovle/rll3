package com.ovle.rll3.model.procedural.grid.util

import com.github.czyzby.noise4j.map.Grid


fun normalize(grid: Grid, newMinValue: Float = 0.0f, newMaxValue: Float = 1.0f) {
    val maxValue = grid.array.max()!!
    val minValue = grid.array.min()!!
    val width = grid.width
    val diff = maxValue - minValue
    val newDiff = newMaxValue - newMinValue

    /*
    boolean shouldStretch = (stretchCoeff != 1);
    if (shouldStretch) {
        newDiff = (int) (stretchCoeff * diff);
        newMinValue = minValue - (newDiff - diff) / 2;
    }*/

    if (diff == newDiff) return

    for (i in 0 until width) {
        val height: Int = grid.height
        for (j in 0 until height) {
            //move
            var normalizedValue = grid[i, j] - minValue
            //squeeze / stretch
            normalizedValue = normalizedValue * newDiff / diff
            //move
            normalizedValue += newMinValue
            grid[i, j] = normalizedValue
        }
    }
}
