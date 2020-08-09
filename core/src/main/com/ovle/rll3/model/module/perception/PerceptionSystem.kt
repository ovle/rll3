package com.ovle.rll3.model.module.perception

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.GameEvent.EntityEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.model.module.core.component.Mappers.perception
import com.ovle.rll3.model.module.core.component.Mappers.position
import com.ovle.rll3.model.module.core.entity.allEntities
import com.ovle.rll3.model.module.core.entity.entitiesWith
import com.ovle.rll3.model.module.core.entity.levelInfo
import com.ovle.rll3.model.module.core.entity.lightObstacles
import com.ovle.rll3.model.module.core.entity.see
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import ktx.ashley.get


class PerceptionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityInitializedEvent> { onEntityInitialized(it.entity) }
        EventBus.subscribe<EntityMovedEvent> { onEntityMoved(it.entity) }
        EventBus.subscribe<UpdateLightCollisionCommand> { onUpdateCollision(it.points) }
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

        val obstacles = levelInfo().entities.lightObstacles()
        perceptionComponent.fov = fov(positionComponent, perceptionComponent, obstacles)

        send(EntityFovUpdatedEvent(entity))
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