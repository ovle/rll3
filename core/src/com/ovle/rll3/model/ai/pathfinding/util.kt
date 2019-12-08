package com.ovle.rll3.model.ai.pathfinding

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.tile.PassTypeFn
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TilePassType

typealias MoveCostFn = ((Tile, Tile?, PassTypeFn) -> Int)

const val maxMoveCost = 9999


fun cost(from: Tile, to: Tile?, passTypeFn: PassTypeFn): Int {
    if (to == null) return maxMoveCost
    return when (passTypeFn(to)) {
        TilePassType.Passable -> 1
        else -> maxMoveCost
    }
}

fun heuristics(from: Tile, to: Tile?, passTypeFn: PassTypeFn): Int {
    if (to == null) return maxMoveCost

    val fromVector = Vector2(from.x.toFloat(), from.y.toFloat())
    val toVector = Vector2(to.x.toFloat(), to.y.toFloat())
    return fromVector.dst(toVector).toInt()
}