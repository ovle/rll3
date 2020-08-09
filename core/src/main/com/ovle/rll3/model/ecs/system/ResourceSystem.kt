package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.source
import com.ovle.rll3.model.ecs.entity.newTemplatedEntity
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.template.entity.entityTemplate
import ktx.ashley.get


class ResourceSystem : EventSystem() {

    override fun subscribe() {
        subscribe<Event.EntityGathered> { onEntityGathered(it.entity, it.amount) }
        subscribe<Event.TimeChanged> { onTimeChanged(it.turn) }
    }

    private fun onEntityGathered(entity: Entity, amount: Int) {
        val sourceComponent = entity[source]!!
        sourceComponent.gatherCostPaid += amount

        if (sourceComponent.isGathered) {
            val gridPosition = entity[position]!!.gridPosition
            val resourceType = sourceComponent.type.name.decapitalize()

            engine.removeEntity(entity)
            send(Event.EntityDestroyed(entity))

            newTemplatedEntity(randomId(), entityTemplate(name = resourceType), engine)
                .apply { this[position]!!.gridPosition = gridPosition }
        }
    }

    private fun onTimeChanged(turn: Turn) {

    }
}
