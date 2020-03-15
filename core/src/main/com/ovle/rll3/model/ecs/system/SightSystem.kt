package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.component.Mappers.sight
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.entity.levelInfo
import com.ovle.rll3.model.ecs.entity.obstacles
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import ktx.ashley.get


class SightSystem : IteratingSystem(Family.all(SightComponent::class.java).get()) {

    //todo dirty / event
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sightComponent = entity[sight]!!
        val positionComponent = entity[position] ?: return
        if (sightComponent.positions.isEmpty()) {
            val obstacles = obstacles(levelInfo())
            sightComponent.positions = fov(positionComponent, sightComponent, obstacles)
        }
    }

    private fun fov(positionComponent: PositionComponent, sightComponent: SightComponent, obstacles: List<GridPoint2>): Set<GridPoint2> {
        return fieldOfView(
            positionComponent.gridPosition,
            sightComponent.radius,
            ::lightTilePassMapper,
            levelInfo().tiles,
            obstacles
        ).toSet()
    }
}