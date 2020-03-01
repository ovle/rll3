package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.entity.entitiesWith
import ktx.ashley.get

class LightComponent(
    val radius: Int,
    val lightPositions: List<LightTilePosition> = listOf()
) : Component

data class LightTilePosition(
    val value: Float,
    val tilePosition: GridPoint2
)

//todo cache / memoize
fun lightTiles(levelInfo: LevelInfo): List<LightTilePosition> {
    val lightSources = entitiesWith(levelInfo.objects, LightComponent::class)
    return lightSources.map { it[Mappers.light]!!.lightPositions }.flatten()
}

fun lightByPosition(lightTiles: List<LightTilePosition>) = lightTiles.groupBy { it.tilePosition }
    .mapValues { it.value.sumByDouble { lightTilePosition -> lightTilePosition.value.toDouble() } }