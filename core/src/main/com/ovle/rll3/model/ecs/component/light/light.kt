package com.ovle.rll3.model.ecs.component.light

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.componentMapper
import com.ovle.rll3.model.ecs.entity.EntityQuery.entitiesWith
import ktx.ashley.get


data class LightTilePosition(
    val value: Float,
    val tilePosition: GridPoint2
)

//todo cache / memoize
fun lightTiles(levelInfo: LevelInfo): List<LightTilePosition> {
    val lightSources = entitiesWith(levelInfo.objects, LightComponent::class)
    val lightMapper = componentMapper<LightComponent>()
    return lightSources.map { it[lightMapper]!!.lightPositions }.flatten()
}

fun lightByPosition(lightTiles: List<LightTilePosition>) = lightTiles.groupBy { it.tilePosition }
    .mapValues { it.value.sumByDouble { lightTilePosition -> lightTilePosition.value.toDouble() } }