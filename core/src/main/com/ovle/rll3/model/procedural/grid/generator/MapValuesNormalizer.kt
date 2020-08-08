package com.ovle.rll3.model.procedural.grid.generator

import com.github.czyzby.noise4j.map.Grid

class MapValuesNormalizer {
    var newMinValue = 0.0f
    var newMaxValue = 1.0f

    //public float stretchCoeff = 1.0f;
    fun process(areaTiles: Grid) {
        val maxValue = areaTiles.array.max()!!
        val minValue = areaTiles.array.min()!!
        val width = areaTiles.width
        val diff = maxValue - minValue
        val newDiff = newMaxValue - newMinValue

        /*
		boolean shouldStretch = (stretchCoeff != 1);
		if (shouldStretch) {
			newDiff = (int) (stretchCoeff * diff);
			newMinValue = minValue - (newDiff - diff) / 2;
		}*/if (diff != newDiff) {
            //normalize
            for (i in 0 until width) {
                val height: Int = areaTiles.height
                for (j in 0 until height) {
                    //move
                    var normalizedValue = areaTiles[i, j] - minValue
                    //squeeze / stretch
                    normalizedValue = normalizedValue * newDiff / diff
                    //move
                    normalizedValue += newMinValue
                    areaTiles[i, j] = normalizedValue
                }
            }
        }
    }
}
