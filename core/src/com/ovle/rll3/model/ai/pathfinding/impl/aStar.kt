package com.ovle.rll3.model.ai.pathfinding.impl

import com.ovle.rll3.model.ai.pathfinding.MoveCost
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues


private fun path(end: Tile, cameFrom: Map<Tile, Tile>): List<Tile> {
    val path = mutableListOf(end)
    var current = end
    while (cameFrom.containsKey(current)) {
        current = cameFrom.getValue(current)
        path.add(0, current)
    }
    return path.toList()
}


fun path(from: Tile, to: Tile, tiles: TileArray, heuristics: MoveCost, cost: MoveCost): List<Tile> {
    val open = mutableSetOf(from)
    val closed = mutableSetOf<Tile>()
    val costFromStart = mutableMapOf(from to 0)
    val estimatedTotalCost = mutableMapOf(from to heuristics(from, to))
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
            val score = costFromStart.getValue(currentPos) + cost(currentPos, neighbour)
            if (score >= costFromStart.getOrDefault(neighbour, Integer.MAX_VALUE)) continue

            val totalScore = score + heuristics(neighbour, to)

            open += neighbour
            cameFrom[neighbour] = currentPos
            costFromStart[neighbour] = score
            estimatedTotalCost[neighbour] = totalScore
        }

    }

    println("no path found from $from to $to")
    return emptyList()
}