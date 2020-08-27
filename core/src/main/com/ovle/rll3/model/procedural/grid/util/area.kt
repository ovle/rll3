package com.ovle.rll3.model.procedural.grid.util

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.Area
import com.ovle.rll3.component1
import com.ovle.rll3.component2

fun Area.apply(grid: Grid, marker: Float) {
    forEach { point ->
        val (x, y) = point
        grid.set(x, y, marker)
    }
}

fun Area.widen(): Collection<GridPoint2> {
    val result = toMutableSet()
    forEach {
        val (x, y) = it
        result.add(com.ovle.rll3.point(x - 1, y))
    }
    return result.toList()
}