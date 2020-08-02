package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.Area
import com.ovle.rll3.point


fun floodFill(grid: Grid, marker: Float): MutableList<Area> {
    val areas = mutableListOf<Area>()
    var areaMarker = 1.0f

    for (x in (0 until grid.width)) {
        for (y in (0 until grid.height)) {
            if (grid[x, y] == marker) {
                val area = mutableListOf<GridPoint2>()
                areas.add(area)

                floodFill(grid, area, x, y, marker, areaMarker)

                areaMarker++
            }
        }
    }

    return areas
}

private fun floodFill(grid: Grid, area: MutableList<GridPoint2>, x: Int, y: Int, marker: Float, areaMarker: Float) {
    if (!grid.isIndexValid(x, y)) return

    val value = grid[x, y]
    if (value != marker) return

    grid.set(x, y, areaMarker)

    area.add(point(x, y))

    floodFill(grid, area, x-1, y, marker, areaMarker)
    floodFill(grid, area, x+1, y, marker, areaMarker)
    floodFill(grid, area, x, y-1, marker, areaMarker)
    floodFill(grid, area, x, y+1, marker, areaMarker)
}