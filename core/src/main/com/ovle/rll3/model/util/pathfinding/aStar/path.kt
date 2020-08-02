package com.ovle.rll3.model.util.pathfinding.aStar

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.*
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.pathfinding.maxMoveCost


private fun path(end: GridPoint2, cameFrom: Map<GridPoint2, GridPoint2>): List<GridPoint2> {
    val path = mutableListOf(end)
    var current = end
    while (cameFrom.containsKey(current)) {
        current = cameFrom.getValue(current)
        path.add(0, current)
    }
    return path.toList()
}


fun path(from: GridPoint2, to: GridPoint2, tiles: TileArray, obstacles: Collection<GridPoint2>, heuristicsFn: MoveCostFn2, costFn: MoveCostFn, tilePassTypeFn: TilePassTypeFn): List<GridPoint2> {
    val open = mutableSetOf(from)
    val closed = mutableSetOf<GridPoint2>()
    val costFromStart = mutableMapOf(from to 0)
    val estimatedTotalCost = mutableMapOf(from to heuristicsFn(from, to, tilePassTypeFn))
    val cameFrom = mutableMapOf<GridPoint2, GridPoint2>()

    while (open.isNotEmpty()) {
        val currentPosition = open.minBy { estimatedTotalCost.getValue(it) }!!
        val (x ,y) = currentPosition
        val currentTile = tiles.tile(x, y)
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

            val neighbourTile = tiles.tile(nX, nY)
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

//    println("no path found from $from to $to")
    return emptyList()
}

private fun nearHV(x: Int, y: Int) = arrayOf(
    point(x - 1, y),
    point(x + 1, y),
    point(x, y - 1),
    point(x, y + 1)
)
