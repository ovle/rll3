package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.advanced.PerceptionComponent
import com.ovle.rll3.model.ecs.component.basic.PositionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.sight
import com.ovle.rll3.model.ecs.entity.levelInfo
import com.ovle.rll3.model.ecs.entity.obstacles
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import ktx.ashley.get


class SightSystem : IteratingSystem(Family.all(PerceptionComponent::class.java).get()) {

    //todo dirty / event
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sightComponent = entity[sight]!!
        val positionComponent = entity[position] ?: return
        if (sightComponent.sightPositions.isEmpty()) {
            val obstacles = obstacles(levelInfo())
            sightComponent.sightPositions = fov(positionComponent, sightComponent, obstacles)
        }
    }

    private fun fov(positionComponent: PositionComponent, perceptionComponent: PerceptionComponent, obstacles: List<GridPoint2>): Set<GridPoint2> {
        val sightRadius = perceptionComponent.sightRadius ?: return setOf()

        return fieldOfView(
            positionComponent.gridPosition,
            sightRadius,
            ::lightTilePassMapper,
            levelInfo().tiles,
            obstacles
        ).toSet()
    }
}