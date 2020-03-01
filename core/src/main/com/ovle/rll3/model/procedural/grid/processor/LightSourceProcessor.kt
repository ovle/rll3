package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.LightTilePosition
import com.ovle.rll3.model.ecs.entity.newLightSource
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.DungeonGenerationSettings
import com.ovle.rll3.model.procedural.grid.floorTypes
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.model.util.config.LightConfig
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import com.ovle.rll3.point


class LightSourceProcessor : TilesProcessor {

    override fun process(tiles: TileArray, generationSettings: LevelGenerationSettings, gameEngine: Engine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        generationSettings as DungeonGenerationSettings

        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)
                val isFloorTile = nearTiles.value?.typeId in floorTypes
                val isFreeSpaceTileNear = nearTiles.allHV.map { it?.typeId }.any { it in floorTypes }
                val isWallTileNear = nearTiles.allHV.map { it?.typeId }.any { it == wallTileId }
                val isFreeForLightSource = isFloorTile && isFreeSpaceTileNear && isWallTileNear
                //todo this should depend on distance to nearest light source
                val isLightSource = isFreeForLightSource && Math.random() <= generationSettings.lightSourceChance
                //todo check doors ?
                if (isLightSource) {
                    val position = point(x, y)
                    result.add(newLightSource(position, gameEngine, lightPositions(position, tiles, LightConfig)))
                }
            }
        }
        return result
    }

    private fun lightPositions(position: GridPoint2, tiles: TileArray, lightConfig: LightConfig): List<LightTilePosition> {
        return fieldOfView(
            position,
            lightConfig.radius,
            ::lightTilePassMapper,
            tiles
        ).map {
            LightTilePosition(lightValue(lightConfig.fullLightCap, position, it), it)
        }
    }

    private fun lightValue(maxLightValue: Float, center: GridPoint2, position: GridPoint2): Float {
        val distance = center.dst(position)
        return (maxLightValue / (distance * distance))
    }
}