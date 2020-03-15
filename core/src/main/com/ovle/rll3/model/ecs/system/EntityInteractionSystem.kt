package com.ovle.rll3.model.ecs.system

import com.ovle.rll3.Event.EntityEvent
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.Mappers.collision
import com.ovle.rll3.model.ecs.component.Mappers.door
import com.ovle.rll3.model.ecs.component.Mappers.render
import com.ovle.rll3.model.ecs.component.has
import ktx.ashley.get


class EntityInteractionSystem : EventSystem<EntityEvent>() {

    override fun channel() = receive<EntityEvent>()

    override fun dispatch(event: EntityEvent) {
        val entity = event.entity

        //todo rewrite to processors
        if (entity.has<DoorComponent>()) {
            entity[door]!!.let { it.closed = !it.closed }
            val closed =  entity[door]!!.closed

            //todo shouldn't know about this here
            entity[render]?.let { it.sprite = null }
            entity[collision]?.let { it.active = closed }
            //todo recalculate nearest lightning sources
        }
    }
}
