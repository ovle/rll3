package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.advanced.PerceptionComponent
import com.ovle.rll3.model.ecs.component.basic.PositionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.perception
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.levelInfo
import com.ovle.rll3.model.ecs.entity.obstacles
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import ktx.ashley.get


class SightSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntityInitialized> { onEntityInitialized(it.entity) }
        EventBus.subscribe<Event.EntityMoved> { onEntityMoved(it.entity) }
    }

    private fun onEntityInitialized(entity: Entity) {
        updateFov(entity)
    }

    private fun onEntityMoved(entity: Entity) {
        updateFov(entity)
    }

    private fun updateFov(entity: Entity) {
        val perceptionComponent = entity[perception] ?: return
        val positionComponent = entity[position] ?: return

        val obstacles = obstacles(levelInfo())
        perceptionComponent.fov = fov(positionComponent, perceptionComponent, obstacles)

        send(Event.EntityFovUpdated(entity))
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