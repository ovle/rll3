package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.advanced.LightSourceComponent
import com.ovle.rll3.model.ecs.component.dto.AOETilePosition
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.entitiesWith
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.config.LightConfig
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import ktx.ashley.get

//todo cache / memoize
fun lightTiles(levelInfo: LevelInfo): List<AOETilePosition> {
    val lightSources = entitiesWith(levelInfo.entities, LightSourceComponent::class)
    return lightSources.map { it[Mappers.light]!!.area.aoePositions }.flatten()
}

fun lightByPosition(AOETiles: List<AOETilePosition>) = AOETiles.groupBy { it.tilePosition }
    .mapValues { it.value.sumByDouble { lightTilePosition -> lightTilePosition.value.toDouble() } }

fun lightPositions(position: GridPoint2, tiles: TileArray, lightConfig: LightConfig, obstacles: List<GridPoint2>): List<AOETilePosition> {
    return fieldOfView(
        position,
        lightConfig.radius,
        ::lightTilePassMapper,
        tiles,
        obstacles
    ).map {
        AOETilePosition(lightValue(lightConfig.fullLightCap, position, it), it)
    }
}

fun lightValue(maxLightValue: Float, center: GridPoint2, position: GridPoint2): Float {
    val distance = center.dst(position)
    return (maxLightValue / (distance * distance))
}