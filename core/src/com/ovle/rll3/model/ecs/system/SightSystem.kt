package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.model.ecs.levelInfo
import com.ovle.rll3.model.tile.lightTilePassMapper
import com.ovle.rll3.model.util.discretization.bresenham.filledCircle
import ktx.ashley.get


class SightSystem : IteratingSystem(Family.all(SightComponent::class.java).get()) {

    private val sight: ComponentMapper<SightComponent> = componentMapper()
    private val position: ComponentMapper<PositionComponent> = componentMapper()

    //todo dirty / event
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sightComponent = entity[sight]!!
        val positionComponent = entity[position] ?: return
        sightComponent.positions = filledCircle(
            positionComponent.position,
            sightComponent.radius,
            ::lightTilePassMapper,
            levelInfo().tiles
        ).toSet()
    }
}