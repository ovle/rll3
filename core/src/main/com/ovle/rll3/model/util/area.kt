package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.*

data class Area(
    val points: MutableSet<GridPoint2> = mutableSetOf()
) {

    fun apply(grid: Grid, marker: Float) {
        points.forEach {
            (x, y) ->
            grid.set(x, y, marker)
        }
    }

    fun apply(tiles: TileArray, value: Tile) {
        points.forEach {
            tiles[it.x, it.y] = value
        }
    }

    fun widen(): Area {
        val newPoints = points.map {
            (x, y) ->
            point(x - 1, y)
        }
        points.addAll(newPoints)
        return this
    }

    operator fun plus(other: Area) = Area((this.points + other.points).toMutableSet())
}