package com.ovle.rll3.model.module.gathering

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.source
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.template.entity.entityTemplate
import ktx.ashley.get


class ResourceSystem : EventSystem() {

    override fun subscribe() {
        subscribe<EntityGatheredEvent> { onEntityGathered(it.entity) }
        subscribe<TimeChangedEvent> { onTimeChanged(it.turn) }
    }

    private fun onEntityGathered(entity: Entity) {
        println("onEntityGathered!")
        val sourceComponent = entity[source]!!  //todo
        val gridPosition = entity[position]!!.gridPosition
        val resourceType = sourceComponent.type.name.decapitalize()

        send(DestroyEntityCommand(entity))

        val template = entityTemplate(name = resourceType)
        send(CreateEntityCommand(template, gridPosition))
    }

    private fun onTimeChanged(turn: Turn) {
        //todo
    }
}
