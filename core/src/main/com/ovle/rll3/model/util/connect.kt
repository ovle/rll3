package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.util.lineOfSight.rayTracing.trace

enum class ConnectionStrategy {
    RemoveUnconnected {
        override fun apply(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float, areas: MutableList<Area>) {
            val resultArea = removeUnconnected(grid, wallTileMarker, areas)
            resultArea.apply(grid, emptyTileMarker)
        }

        private fun removeUnconnected(grid: Grid, wallTileMarker: Float, areas: MutableList<Area>): Area {
            val mainArea = areas.maxBy { it.points.size }!!
            val areasToRemove = areas.toMutableList().apply { remove(mainArea) }
            areasToRemove.forEach {
                it.apply(grid, wallTileMarker)
            }
            return mainArea
        }
    },

    ConnectUnconnectedWithPath {
        override fun apply(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float, areas: MutableList<Area>) {
            val disconnectedAreas = areas.toMutableList()
            while (disconnectedAreas.size > 1) {
                val area = disconnectedAreas.first()
                val otherAreas = disconnectedAreas.filter { other -> area != other }
                val (from, to, otherArea) = closestPoints(area, otherAreas)
                val points = trace(from, to, emptyList()).toMutableSet()
                val path = Area(points).widen()

                disconnectedAreas.remove(area)
                disconnectedAreas.remove(otherArea)
                val newArea = area + otherArea + path
                disconnectedAreas.add(newArea)
            }

            val result = disconnectedAreas.single()
            result.apply(grid, emptyTileMarker)
        }

        //todo O(n^3)
        private fun closestPoints(area: Area, otherAreas: List<Area>): Triple<GridPoint2, GridPoint2, Area> {
            val pointsInfo = area.points.map { point -> point to closestPointInAreas(point, otherAreas) }
            val closestPointsInfo = pointsInfo.minBy { it.first.dst2(it.second.first) }!!
            //todo rewrite, not readable
            return Triple(closestPointsInfo.first, closestPointsInfo.second.first, closestPointsInfo.second.second)
        }

        private fun closestPointInAreas(point: GridPoint2, otherAreas: List<Area>): Pair<GridPoint2, Area> {
            val points = otherAreas.map { closestPointInArea(point, it)}
            return points.minBy { point.dst2(it.first) }!!
        }

        private fun closestPointInArea(point: GridPoint2, otherArea: Area): Pair<GridPoint2, Area> {
            return otherArea.points.minBy { point.dst2(it) }!! to otherArea
        }
    };

    abstract fun apply(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float, areas: MutableList<Area>)
}

fun connect(grid: Grid, emptyTileMarker: Float, wallTileMarker: Float, connectionStrategy: ConnectionStrategy) {
    val areas = floodFill(grid, emptyTileMarker)
//    println("areas: ${areas.size}")

    connectionStrategy.apply(grid, emptyTileMarker, wallTileMarker, areas)
}