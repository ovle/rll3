package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.TrapComponent
import com.ovle.rll3.model.procedural.trapChance
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTileId

class TrapProcessor : TilesInfoProcessor {

    override fun process(tiles: TileArray, gameEngine: GameEngine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
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

    private fun trap(x: Int, y: Int, gameEngine: GameEngine): Entity {
        return gameEngine.entity(
            PositionComponent(Vector2(x.toFloat(), y.toFloat())),
            TrapComponent()
        )
    }
}