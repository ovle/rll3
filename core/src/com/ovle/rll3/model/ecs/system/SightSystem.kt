package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.SightComponent
import com.ovle.rll3.model.ecs.get
import com.ovle.rll3.model.tile.lightTilePassMapper
import com.ovle.rll3.model.util.discretization.bresenham.circle
import com.ovle.rll3.model.util.lineOfSight.rayTracing.trace
import ktx.ashley.get
import kotlin.math.roundToInt


class SightSystem : IteratingSystem(Family.all(SightComponent::class.java).get()) {

    private val sight: ComponentMapper<SightComponent> = get()
    private val position: ComponentMapper<PositionComponent> = get()

    //todo dirty / event
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sightComponent = entity[sight]!!
        val positionComponent = entity[position] ?: return

        val entityPosition = positionComponent.position
        val visiblePositionsCircle = circle(entityPosition, sightComponent.radius)
        val roundedEntityPosition = entityPosition.x.roundToInt() to entityPosition.y.roundToInt()
        val visiblePositions = visiblePositionsCircle.flatMap {
            trace(roundedEntityPosition, it, ::lightTilePassMapper)
        }.toSet()

        sightComponent.positions = visiblePositions
    }
}