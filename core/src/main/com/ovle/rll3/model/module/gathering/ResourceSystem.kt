package com.ovle.rll3.model.module.gathering

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.Tile
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.module.core.component.ComponentMappers.source
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.template.entity.entityTemplate
import ktx.ashley.get


class ResourceSystem : EventSystem() {

    override fun subscribe() {
        subscribe<EntityGatheredEvent> { onEntityGatheredEvent(it.entity) }
        subscribe<TileGatheredEvent> { onTileGatheredEvent(it.tile, it.position) }
        subscribe<TurnChangedEvent> { onTimeChanged(it.turn) }
    }

    private fun onEntityGatheredEvent(entity: Entity) {
        val sourceComponent = entity[source]!!  //todo
        val gridPosition = entity.position()
        val resourceType = sourceComponent.type.name.decapitalize()

        send(DestroyEntityCommand(entity))

        val template = entityTemplate(name = resourceType)
        send(CreateEntityCommand(template, gridPosition))
    }

    private fun onTileGatheredEvent(tile: Tile, position: GridPoint2) {
        val resourceType = "stone"   //todo
        val template = entityTemplate(name = resourceType)
        send(CreateEntityCommand(template, position))
    }

    private fun onTimeChanged(turn: Turn) {
        //todo
    }
}
