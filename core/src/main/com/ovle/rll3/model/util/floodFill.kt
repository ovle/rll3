package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.*

//todo rewrite
fun floodFillGrid(grid: Grid, marker: Float): MutableList<Area> {
    val areas = mutableListOf<Area>()
//    var areaMarker = 1.0f
//    fun check(value: Float) = value == areaMarker
//
//    for (x in (0 until grid.width)) {
//        for (y in (0 until grid.height)) {
//            val value = grid[x, y]
//            if (value == marker) {
//                areas.add(floodFill(x, y, grid, areaMarker, ::check))
//                areaMarker++
//            }
//        }
//    }

    return areas
}

fun floodFill(x: Int, y: Int, grid: Grid, check: ValueCheck): Area {
    val areaPoints = mutableSetOf<GridPoint2>()

    val nextPoints = mutableSetOf(point(x, y))

    while (nextPoints.isNotEmpty()) {
        val p = nextPoints.first()
        nextPoints.remove(p)

        if (p in areaPoints) continue

        val (x, y) = p
        if (!grid.isIndexValid(x, y)) continue

        val value = grid[x, y]
        if (!check.invoke(value)) continue

//        println("floodFill ($x $y) value: $value")
        areaPoints.add(p)

        nextPoints.addAll(p.nearHV())
    }

    return Area(areaPoints)
}