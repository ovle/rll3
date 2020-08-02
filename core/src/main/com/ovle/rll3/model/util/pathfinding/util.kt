package com.ovle.rll3.model.util.pathfinding

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.TilePassTypeFn
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TilePassType
import kotlin.math.roundToInt


const val maxMoveCost = 9999

fun cost(from: Tile, to: Tile?, tilePassTypeFn: TilePassTypeFn): Int {
    if (to == null) return maxMoveCost
    return when (tilePassTypeFn(to)) {
        TilePassType.Passable -> 1
        else -> maxMoveCost
    }
}

fun heuristics(from: GridPoint2, to: GridPoint2?, tilePassTypeFn: TilePassTypeFn): Int {
    if (to == null) return maxMoveCost
    return from.dst(to).roundToInt()
}