package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.controlledEntity
import com.ovle.rll3.model.ecs.entity.newTemplatedEntity
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.entity.entityTemplate
import ktx.ashley.get


class ContainerSystem : EventSystem() {

    override fun subscribe() {
        subscribe<Event.EntityInitialized> { onEntityInitialized(it.entity) }
        subscribe<Event.DebugShowPlayerInventoryEvent> { onDebugShowEntityInventoryEvent(controlledEntity()!!) }
        subscribe<Event.EntityContentInteraction> { onEntityContentInteraction(it.source, it.entity) }
    }

    private fun onDebugShowEntityInventoryEvent(entity: Entity) {
        val containerComponent = entity[Mappers.container] ?: return
        send(Event.DebugShowInventoryEvent(containerComponent.items, entity))
    }


    private fun onEntityInitialized(entity: Entity) {
        val containerComponent = entity[Mappers.container] ?: return
        if (containerComponent.initialized) return

        val content = spawnContent()
        with(containerComponent) {
            items.clear()
            items.addAll(content)

            initialized = true
        }
    }

    private fun spawnContent(): List<Entity> {
        //todo test
        return listOf(
            newTemplatedEntity("i1", entityTemplate(TemplatesType.Common, "test item"), engine)
        )
    }

    private fun onEntityContentInteraction(source: Entity, entity: Entity) {
        val containerComponent = entity[Mappers.container]!!
        val sourceContainerComponent = source[Mappers.container]

        //todo use gui if player entity
        sourceContainerComponent?.let {
            val items = containerComponent.items.toList()
            if (items.isNotEmpty()) {
                containerComponent.items.clear()
                it.items.addAll(items)

                send(Event.EntityTakeItems(source, items))
            }
        }
    }
}
