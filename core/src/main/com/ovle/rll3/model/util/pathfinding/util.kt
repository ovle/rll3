package com.ovle.rll3.model.util.pathfinding

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TilePassType
import com.ovle.rll3.model.tile.TilePassTypeFn
import kotlin.math.roundToInt

typealias MoveCostFn = ((Tile, Tile?, TilePassTypeFn) -> Int)
typealias IsPassableFn = ((GridPoint2, TilePassTypeFn) -> Boolean)

const val maxMoveCost = 9999


fun cost(from: Tile, to: Tile?, tilePassTypeFn: TilePassTypeFn): Int {
    if (to == null) return maxMoveCost
    return when (tilePassTypeFn(to)) {
        TilePassType.Passable -> 1
        else -> maxMoveCost
    }
}

fun heuristics(from: Tile, to: Tile?, tilePassTypeFn: TilePassTypeFn): Int {
    if (to == null) return maxMoveCost
    return from.position.dst(to.position).roundToInt()
}