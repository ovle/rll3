package com.ovle.rll3.model.ecs.system.event

import com.ovle.rll3.Event.EntityInteractionEvent
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.model.ecs.component.CreatureComponent
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.Mappers.animation
import com.ovle.rll3.model.ecs.component.Mappers.collision
import com.ovle.rll3.model.ecs.component.Mappers.creature
import com.ovle.rll3.model.ecs.component.Mappers.door
import com.ovle.rll3.model.ecs.component.Mappers.render
import com.ovle.rll3.model.ecs.component.has
import ktx.ashley.get


class EntityInteractionSystem : EventSystem<EntityInteractionEvent>() {

    override fun channel() = receive<EntityInteractionEvent>()

    //todo rewrite to processors
    override fun dispatch(event: EntityInteractionEvent) {

        val entity = event.entity

        if (entity.has<DoorComponent>()) {
            entity[door]!!.let { it.closed = !it.closed }
            val closed =  entity[door]!!.closed

            //todo shouldn't know about this here
            entity[render]?.let { it.sprite = null }
            entity[collision]?.let { it.active = closed }
            //todo recalculate nearest lightning sources
        }

        //todo state machine?
        if (entity.has<CreatureComponent>()) {
            if (entity[creature]!!.health == 0) {
                entity[animation]?.startAnimation("resurrect")
                entity[creature]!!.health = 3   //todo initial health from template
            } else {
                entity[creature]!!.health--

                val isDead = entity[creature]!!.health == 0
                entity[animation]?.startAnimation(if (isDead) "death" else "damaged")
            }
        }
    }
}
