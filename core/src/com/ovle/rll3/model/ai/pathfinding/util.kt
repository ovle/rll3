package com.ovle.rll3.model.ai.pathfinding

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.tile.Tile

typealias MoveCost = ((Tile, Tile?) -> Int)

fun cost(from: Tile, to: Tile?): Int = 1

fun heuristics(from: Tile, to: Tile?): Int {
    if (to == null) return Integer.MAX_VALUE

    val fromVector = Vector2(from.x.toFloat(), from.y.toFloat())
    val toVector = Vector2(to.x.toFloat(), to.y.toFloat())
    return fromVector.dst(toVector).toInt()
}