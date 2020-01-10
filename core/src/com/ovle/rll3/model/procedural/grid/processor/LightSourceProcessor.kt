package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.LightTilePosition
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.TilePosition
import com.ovle.rll3.model.ecs.entity
import com.ovle.rll3.model.procedural.floorTypes
import com.ovle.rll3.model.procedural.lightSourceChance
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.lightTilePassMapper
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.model.util.config.LightConfig
import com.ovle.rll3.model.util.discretization.bresenham.filledCircle


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
                //todo this should depend on distance to nearest light source
                val isLightSource = isFreeForLightSource && Math.random() <= lightSourceChance
                //todo check doors ?
                if (isLightSource) {
                    result.add(lightSource(x, y, gameEngine, tiles, LightConfig))
                }
            }
        }
        return result
    }

    private fun lightSource(x: Int, y: Int, gameEngine: Engine, tiles: TileArray, lightConfig: LightConfig) =
        gameEngine.entity(
            PositionComponent(Vector2(x.toFloat(), y.toFloat())),
            LightComponent(lightConfig.radius, lightPositions(x, y, tiles, lightConfig))
        )

    private fun lightPositions(x: Int, y: Int, tiles: TileArray, lightConfig: LightConfig): List<LightTilePosition> {
        val center = Vector2(x.toFloat(), y.toFloat())
        val result = filledCircle(
            center,
            lightConfig.radius,
            ::lightTilePassMapper,
            tiles
        ).map {
            LightTilePosition(lightValue(lightConfig.fullLightCap, center, it), it)
        }

        //todo
        val addTiles = mutableListOf<LightTilePosition>()
        val lightsByPosition = result.associate { it.tilePosition to it.value }
        //due to light reflection light tile cannot exist near dark tile without half-light tile between them
//        result.filter { it.value >= LightConfig.fullLightValue }
//            .forEach {
//                val (x, y) = it.tilePosition
//                addTiles(lightsByPosition, x - 1, y, addTiles)
//                addTiles(lightsByPosition, x + 1, y, addTiles)
//                addTiles(lightsByPosition, x, y - 1, addTiles)
//                addTiles(lightsByPosition, x, y + 1, addTiles)
//            }
        //todo skip solid tiles
//      fun addTiles(lightsByPosition: Map<TilePosition, Int>, checkX: Int, checkY: Int, addTiles: MutableList<LightTilePosition>) {
//         if (lightsByPosition[checkX to checkY] == null) addTiles.add(LightTilePosition(1, checkX to checkY))
//      }

        return result + addTiles
    }

    private fun lightValue(maxLightValue: Float, center: Vector2, position: TilePosition): Float {
        val (x, y) = position
        val distance = center.dst(x.toFloat(), y.toFloat())
        //todo
        return (maxLightValue / (distance * distance))
    }
}