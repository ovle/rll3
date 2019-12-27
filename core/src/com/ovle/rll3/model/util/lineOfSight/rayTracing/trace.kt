package com.ovle.rll3.model.util.lineOfSight.rayTracing

import com.ovle.rll3.model.ecs.component.TilePosition
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.tile.LightPassTypeFn
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.discretization.bresenham.line


fun trace(fromPosition: TilePosition, toPosition: TilePosition, lightPassTypeFn: LightPassTypeFn, tiles: TileArray): List<TilePosition> {
    val positions = line(fromPosition, toPosition)
    // todo add last to see the walls
    return positions.takeWhile { losPassable(it, lightPassTypeFn, tiles) }
}

private fun losPassable(tilePosition: TilePosition, lightPassTypeFn: LightPassTypeFn, tiles: TileArray): Boolean {
    val tile = tiles[tilePosition.first, tilePosition.second] ?: return false
    return when(lightPassTypeFn(tile)) {
        LightPassType.Passable -> true
        else -> false
    }
}