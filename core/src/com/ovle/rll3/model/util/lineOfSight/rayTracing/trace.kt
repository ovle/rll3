package com.ovle.rll3.model.util.lineOfSight.rayTracing

import com.ovle.rll3.model.ecs.component.TilePosition
import com.ovle.rll3.model.ecs.component.tileMap
import com.ovle.rll3.model.tile.LightPassType
import com.ovle.rll3.model.tile.LightPassTypeFn
import com.ovle.rll3.model.util.discretization.bresenham.line


fun trace(fromPosition: TilePosition, toPosition: TilePosition, lightPassTypeFn: LightPassTypeFn): List<TilePosition> {
    val positions = line(fromPosition, toPosition)
    // todo add last to see the walls
    return positions.takeWhile { losPassable(it, lightPassTypeFn) }
}

fun losPassable(tilePosition: TilePosition, lightPassTypeFn: LightPassTypeFn): Boolean {
    //todo global data usage
    val tile = tileMap[tilePosition.first, tilePosition.second] ?: return false
    return when(lightPassTypeFn(tile)) {
        LightPassType.Passable -> true
        else -> false
    }
}