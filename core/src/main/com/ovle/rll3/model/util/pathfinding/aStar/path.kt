package com.ovle.rll3.model.util.pathfinding.aStar

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.*
import com.ovle.rll3.model.module.core.entity.bodyObstacles
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.tile.tilePassType
import com.ovle.rll3.model.util.pathfinding.cost
import com.ovle.rll3.model.util.pathfinding.heuristics
import com.ovle.rll3.model.util.pathfinding.maxMoveCost


fun path(from: GridPoint2, to: GridPoint2, location: LocationInfo): List<GridPoint2> = path(
    from,
    to,
    location.tiles,
    location.entities.bodyObstacles(),
    heuristicsFn = ::heuristics,
    costFn = ::cost,
    tilePassTypeFn = ::tilePassType
)


fun path(from: GridPoint2, to: GridPoint2, tiles: TileArray, obstacles: Collection<GridPoint2>, heuristicsFn: MoveCostFn2, costFn: MoveCostFn, tilePassTypeFn: TilePassTypeFn): List<GridPoint2> {
    val open = mutableSetOf(from)
    val closed = mutableSetOf<GridPoint2>()
    val costFromStart = mutableMapOf(from to 0)
    val estimatedTotalCost = mutableMapOf(from to heuristicsFn(from, to, tilePassTypeFn))
    val cameFrom = mutableMapOf<GridPoint2, GridPoint2>()

    while (open.isNotEmpty()) {
        val currentPosition = open.minBy { estimatedTotalCost.getValue(it) }!!
        val (x, y) = currentPosition
        val currentTile = tiles[x, y]
        if (currentPosition == to) {
            return path(currentPosition, cameFrom)
        }

        open -= currentPosition
        closed += currentPosition

        val nearValues = nearHV(x, y)
            .filterNot { it in closed }

        for (neighbour in nearValues) {
            val (nX, nY) = neighbour
            if (!tiles.isPointValid(nX, nY)) continue
            if (obstacles.contains(point(nX, nY))) continue

            val neighbourTile = tiles[nX, nY]
            val nextCost = costFn(currentTile, neighbourTile, tilePassTypeFn)
            if (nextCost == maxMoveCost) continue

            val score = costFromStart.getValue(currentPosition) + nextCost
            if (score >= costFromStart.getOrDefault(neighbour, Int.MAX_VALUE)) continue

            val totalScore = score + heuristicsFn(neighbour, to, tilePassTypeFn)

            open += neighbour
            cameFrom[neighbour] = currentPosition
            costFromStart[neighbour] = score
            estimatedTotalCost[neighbour] = totalScore
        }

    }

    return emptyList()
}

private fun path(end: GridPoint2, cameFrom: Map<GridPoint2, GridPoint2>): List<GridPoint2> {
    val path = mutableListOf(end)
    var current = end
    while (cameFrom.containsKey(current)) {
        current = cameFrom.getValue(current)
        path.add(0, current)
    }
    return path.toList()
}
