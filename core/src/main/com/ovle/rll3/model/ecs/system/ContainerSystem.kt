package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get


class ContainerSystem : EventSystem() {

    override fun subscribe() {
        subscribe<Event.EntityContentInteraction> { onEntityContentInteraction(it.source, it.entity) }
    }

    private fun onEntityContentInteraction(source: Entity, entity: Entity) {
        val containerComponent = entity[Mappers.container]!!
        val sourceContainerComponent = source[Mappers.container]

        //todo use gui if player entity
        sourceContainerComponent?.let {
            val items = containerComponent.items
            if (items.isNotEmpty()) {
                it.items.addAll(items)
                send(Event.EntityTakeItems(source, items))
            }
        }
    }
}
