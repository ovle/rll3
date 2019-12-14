package com.ovle.rll3.model.util.pathfinding

import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.TilePosition
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TilePassType
import com.ovle.rll3.model.tile.TilePassTypeFn

typealias MoveCostFn = ((Tile, Tile?, TilePassTypeFn) -> Int)
typealias IsPassableFn = ((TilePosition, TilePassTypeFn) -> Boolean)

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

    val fromVector = Vector2(from.x.toFloat(), from.y.toFloat())
    val toVector = Vector2(to.x.toFloat(), to.y.toFloat())
    return fromVector.dst(toVector).toInt()
}