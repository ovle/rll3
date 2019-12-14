package com.ovle.rll3.model.util.pathfinding.aStar

import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.TilePassTypeFn
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.util.pathfinding.MoveCostFn


private fun path(end: Tile, cameFrom: Map<Tile, Tile>): List<Tile> {
    val path = mutableListOf(end)
    var current = end
    while (cameFrom.containsKey(current)) {
        current = cameFrom.getValue(current)
        path.add(0, current)
    }
    return path.toList()
}


fun path(from: Tile, to: Tile, tiles: TileArray, heuristicsFn: MoveCostFn, costFn: MoveCostFn, tilePassTypeFn: TilePassTypeFn): List<Tile> {
    val open = mutableSetOf(from)
    val closed = mutableSetOf<Tile>()
    val costFromStart = mutableMapOf(from to 0)
    val estimatedTotalCost = mutableMapOf(from to heuristicsFn(from, to, tilePassTypeFn))
    val cameFrom = mutableMapOf<Tile, Tile>()

    while (open.isNotEmpty()) {
        val currentPos = open.minBy { estimatedTotalCost.getValue(it) }!!
        if (currentPos == to) {
            return path(currentPos, cameFrom)
        }

        open -= currentPos
        closed += currentPos

        val nearValues = nearValues(tiles, currentPos.x, currentPos.y).nearHV
            .filterNotNull()
            .filterNot { it in closed }

        for (neighbour in nearValues) {
            val score = costFromStart.getValue(currentPos) + costFn(currentPos, neighbour, tilePassTypeFn)
            if (score >= costFromStart.getOrDefault(neighbour, Int.MAX_VALUE)) continue

            val totalScore = score + heuristicsFn(neighbour, to, tilePassTypeFn)

            open += neighbour
            cameFrom[neighbour] = currentPos
            costFromStart[neighbour] = score
            estimatedTotalCost[neighbour] = totalScore
        }

    }

    println("no path found from $from to $to")
    return emptyList()
}