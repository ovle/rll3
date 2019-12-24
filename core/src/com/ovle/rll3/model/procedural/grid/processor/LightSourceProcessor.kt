package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.procedural.floorTypes
import com.ovle.rll3.model.procedural.lightSourceChance
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.wallTileId

class LightSourceProcessor : TilesInfoProcessor {

    override fun process(tiles: TileArray, gameEngine: GameEngine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        for (x in 0 until tiles.width) {
            for (y in 0 until tiles.height) {
                val nearTiles = nearValues(tiles, x, y)
                val isFloorTile = nearTiles.value?.typeId in floorTypes
                val isFreeSpaceTileNear = nearTiles.allHV.map { it?.typeId }.any { it in floorTypes }
                val isWallTileNear = nearTiles.allHV.map { it?.typeId }.any { it == wallTileId }
                val isFreeForLightSource = isFloorTile && isFreeSpaceTileNear && isWallTileNear
                val isLightSource = isFreeForLightSource && Math.random() <= lightSourceChance
                //todo check doors ?
                if (isLightSource) {
                    result.add(lightSource(x, y, gameEngine))
                }
            }
        }
        return result
    }

    private fun lightSource(x: Int, y: Int, gameEngine: GameEngine): Entity {
        return gameEngine.entity(
            PositionComponent(Vector2(x.toFloat(), y.toFloat())),
            LightComponent(5)
        )
    }
}