package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.model.ecs.levelInfo
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import ktx.ashley.get


class SightSystem : IteratingSystem(Family.all(SightComponent::class.java).get()) {

    private val sight: ComponentMapper<SightComponent> = componentMapper()
    private val position: ComponentMapper<PositionComponent> = componentMapper()

    //todo dirty / event
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sightComponent = entity[sight]!!
        val positionComponent = entity[position] ?: return
        if (sightComponent.positions.isEmpty()) {
            sightComponent.positions = fov(positionComponent, sightComponent)
        }
    }

    private fun fov(positionComponent: PositionComponent, sightComponent: SightComponent): Set<GridPoint2> {
        return fieldOfView(
            positionComponent.position,
            sightComponent.radius,
            ::lightTilePassMapper,
            levelInfo().tiles
        ).toSet()
    }
}