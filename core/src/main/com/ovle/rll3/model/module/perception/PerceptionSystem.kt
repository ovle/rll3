package com.ovle.rll3.model.module.perception

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.event.EventBus
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.model.module.core.component.ComponentMappers.perception
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.allEntities
import com.ovle.rll3.model.module.core.entity.entitiesWith
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.entity.lightObstacles
import com.ovle.rll3.model.module.core.entity.see
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rlUtil.gdx.math.lineOfSight.rayTracing.fieldOfView
import com.ovle.rll3.event.EntityFovUpdatedEvent
import com.ovle.rll3.event.EntityInitializedEvent
import com.ovle.rll3.event.EntityMovedEvent
import com.ovle.rll3.event.UpdateLightCollisionCommand
import ktx.ashley.get


class PerceptionSystem : EventSystem() {

    override fun subscribe() {
        subscribe<EntityInitializedEvent> { onEntityInitialized(it.entity) }
        subscribe<EntityMovedEvent> { onEntityMoved(it.entity) }
        subscribe<UpdateLightCollisionCommand> { onUpdateCollision(it.points) }
    }

    private fun onUpdateCollision(points: Array<GridPoint2>) {
        val entitiesWithSight = entitiesWith(allEntities().toList(), PerceptionComponent::class)
        val entitiesToUpdate = entitiesWithSight.filter { e -> points.any { e.see(it) } }

        entitiesToUpdate.forEach {
            updateFov(it)
        }
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

        val obstacles = locationInfo().entities.lightObstacles()
        perceptionComponent.fov = fov(positionComponent, perceptionComponent, obstacles)

        send(EntityFovUpdatedEvent(entity))
    }

    private fun fov(positionComponent: PositionComponent, perceptionComponent: PerceptionComponent, obstacles: List<GridPoint2>): Set<GridPoint2> {
        val sightRadius = perceptionComponent.sightRadius ?: return setOf()

        return fieldOfView(
            positionComponent.gridPosition,
            sightRadius,
            ::lightTilePassMapper,
            locationInfo().tiles,
            obstacles
        ).toSet()
    }
}