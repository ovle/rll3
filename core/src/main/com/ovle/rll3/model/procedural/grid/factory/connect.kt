package com.ovle.rll3.model.procedural.grid.factory

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.model.util.lineOfSight.rayTracing.trace
import com.ovle.rll3.point

//todo utests
//todo extract path functions

enum class ConnectionStrategy {
    RemoveUnconnected {
        private fun removeUnconnected(grid: Grid, wallTileMarker: Float, areas: MutableList<MutableList<GridPoint2>>): Area {
            val mainArea = areas.maxBy { it.size }!!
            println("mainArea size: ${mainArea.size}")

            val areasToRemove = areas.toMutableList().apply { remove(mainArea) }
            areasToRemove.forEach {
                it.apply(grid, wallTileMarker)
            }
            return mainArea
        }

        override fun apply(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float, areas: MutableList<Area>) {
            val resultArea = removeUnconnected(grid, wallTileMarker, areas)
            resultArea.apply(grid, emptyTileMarker)
        }
    },

    ConnectUnconnectedWithPath {
        override fun apply(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float, areas: MutableList<Area>) {
            areas.forEach {
                it.apply(grid, emptyTileMarker)
            }

            areas.forEach {
                val closest = areas.filter { other -> it != other }.random()    //todo
                val from = it.random()
                val to = closest.random()
                val path = trace(from, to, emptyList()).widen()
                path.apply(grid, emptyTileMarker)
            }
        }
    };

    abstract fun apply(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float, areas: MutableList<Area>)
}

fun connect(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float) {
    //todo
    val areas = floodFill(grid, emptyTileMarker)
//    val areas = floodFill(grid.copy(), emptyTileMarker)
    println("areas: ${areas.size}")

    val connectionStrategy = ConnectionStrategy.ConnectUnconnectedWithPath
//    val connectionStrategy = ConnectionStrategy.RemoveUnconnected
    connectionStrategy.apply(grid, emptyTileMarker, wallTileMarker, areas)
}

private fun Collection<GridPoint2>.apply(grid: Grid, marker: Float) {
    forEach { point ->
        val (x, y) = point
        grid.set(x, y, marker)
    }
}

private fun Collection<GridPoint2>.widen(): MutableList<GridPoint2> {
    val result = toMutableList()
    //todo
    return result
}

typealias Area = MutableList<GridPoint2>

private fun floodFill(grid: Grid, emptyTileMarker: Float): MutableList<Area> {
    val areas = mutableListOf<Area>()
    var areaMarker = 1.0f

    for (x in (0 until grid.width)) {
        for (y in (0 until grid.height)) {
            if (grid[x, y] == emptyTileMarker) {
                val area = mutableListOf<GridPoint2>()
                areas.add(area)

                floodFill(grid, area, x, y, emptyTileMarker, areaMarker)

                areaMarker++
            }
        }
    }

    return areas
}

private tailrec fun floodFill(grid: Grid, area: Area, x: Int, y: Int, emptyTileMarker: Float, areaMarker: Float) {
    if (!grid.isIndexValid(x, y)) return

    val value = grid[x, y]
    if (value != emptyTileMarker) return

    grid.set(x, y, areaMarker)

    area.add(point(x, y))

    floodFill(grid, area, x-1, y, emptyTileMarker, areaMarker)
    floodFill(grid, area, x+1, y, emptyTileMarker, areaMarker)
    floodFill(grid, area, x, y-1, emptyTileMarker, areaMarker)
    floodFill(grid, area, x, y+1, emptyTileMarker, areaMarker)
}