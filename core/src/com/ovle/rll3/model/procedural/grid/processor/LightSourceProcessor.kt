package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.LightTilePosition
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.entity
import com.ovle.rll3.model.procedural.floorTypes
import com.ovle.rll3.model.procedural.lightSourceChance
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.lightTilePassMapper
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.model.util.config.LightConfig
import com.ovle.rll3.model.util.discretization.bresenham.filledCircle
import kotlin.math.roundToInt


class LightSourceProcessor : TilesInfoProcessor {

    override fun process(tiles: TileArray, gameEngine: Engine): Collection<Entity> {
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
                val radius = LightConfig.fullRadius
                if (isLightSource) {
                    result.add(lightSource(x, y, radius, gameEngine, tiles))
                }
            }
        }
        return result
    }

    private fun lightSource(x: Int, y: Int, radius: Int, gameEngine: Engine, tiles: TileArray) =
        gameEngine.entity(
            PositionComponent(Vector2(x.toFloat(), y.toFloat())),
            LightComponent(radius, lightPositions(x, y, radius, tiles))
        )

    private fun lightPositions(x: Int, y: Int, radius: Int, tiles: TileArray): List<LightTilePosition> {
        val center = Vector2(x.toFloat(), y.toFloat())
        return filledCircle(
            center,
            radius,
            ::lightTilePassMapper,
            tiles
        ).map {
            val value = radius - center.dst2(it.first.toFloat(), it.second.toFloat()).roundToInt()
            LightTilePosition(value, it)
        }
    }
}