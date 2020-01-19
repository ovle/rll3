package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.TrapComponent
import com.ovle.rll3.model.ecs.component.point
import com.ovle.rll3.model.ecs.entity
import com.ovle.rll3.model.procedural.trapChance
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTileId

class TrapProcessor : TilesInfoProcessor {

    override fun process(tiles: TileArray, gameEngine: Engine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)
                val isRoomFloorTile = nearTiles.value?.typeId == roomFloorTileId
                val isFreeForTrap = isRoomFloorTile
                val isTrap = isFreeForTrap && Math.random() <= trapChance
                if (isTrap) {
                    result.add(trap(x, y, gameEngine))
                }
            }
        }
        return result
    }

    private fun trap(x: Int, y: Int, gameEngine: Engine): Entity {
        return gameEngine.entity(
            PositionComponent(point(x, y)),
            TrapComponent()
        )
    }
}