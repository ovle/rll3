package com.ovle.rll3.model.util.lineOfSight.rayTracing

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.tile.LightPassTypeFn
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.discretization.bresenham.filledCircle
import com.ovle.rll3.model.util.discretization.bresenham.line


fun trace(fromPosition: GridPoint2, toPosition: GridPoint2, obstacles: Collection<GridPoint2>): List<GridPoint2> {
    val positions = line(fromPosition, toPosition)
    val result = positions.takeWhile { it !in obstacles }
    //add last to see the walls
    //if (result.size < positions.size) result += positions[result.size]
//    return positions.toList()
    return result
}

private fun losPassable(tilePosition: GridPoint2, lightPassTypeFn: LightPassTypeFn, tiles: TileArray): Boolean {
    val (x, y) = tilePosition
    val tile = tiles.tile(x, y)

    return lightPassTypeFn(tile) == LightPassType.Passable
}

fun cropArea(area: Collection<GridPoint2>, tiles: TileArray) = area.filter { tiles.isPointValid(it.x, it.y) }

fun obstacles(area: Collection<GridPoint2>, passMapper: LightPassTypeFn, tiles: TileArray): Collection<GridPoint2> {
    return area.filterNot {
        losPassable(it, passMapper, tiles)
    }.toSet()
}

private fun traceFov(center: GridPoint2, area: List<GridPoint2>, obstacles: Collection<GridPoint2>): List<GridPoint2> {
    val result = mutableSetOf<GridPoint2>()
    for (point in area) {
        if (point in result) continue

        result.addAll(trace(center, point, obstacles))
    }
    return result.toList()
}

fun fieldOfView(center: GridPoint2, radius: Int, passMapper: (Tile) -> LightPassType, tiles: TileArray, obstacles: Collection<GridPoint2>): List<GridPoint2> {
    val area = filledCircle(center, radius)
    val croppedArea = cropArea(area, tiles)
    val obstaclesTiles = obstacles(croppedArea, passMapper, tiles)
    return traceFov(center, croppedArea, obstaclesTiles + obstacles)
}