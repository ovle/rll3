package com.ovle.rll3.model.ecs.system

import com.ovle.rll3.Event.EntityEvent
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.Mappers
import com.ovle.rll3.model.ecs.component.has
import ktx.ashley.get


class EntityInteractionSystem : EventSystem<EntityEvent>() {

    override fun channel() = receive<EntityEvent>()

    override fun dispatch(event: EntityEvent) {
        val entity = event.entity

        //todo rewrite to processors
        if (entity.has<DoorComponent>()) {
            val doorComponent = entity[Mappers.door]!!
            doorComponent.closed = !doorComponent.closed
            //todo shouln't know about render here
            val renderComponent = entity[Mappers.render]
            renderComponent?.let {
                it.sprite = null
            }
        }
    }
}
