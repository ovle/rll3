package com.ovle.rll3.model.ecs.system.event

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.AnimationType
import com.ovle.rll3.model.ecs.component.CreatureComponent
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.Mappers.collision
import com.ovle.rll3.model.ecs.component.Mappers.creature
import com.ovle.rll3.model.ecs.component.Mappers.door
import com.ovle.rll3.model.ecs.component.Mappers.render
import com.ovle.rll3.model.ecs.component.has
import ktx.ashley.get


class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntityInteractionEvent> { onEntityInteractionEvent(it.entity) }
    }

    //todo rewrite to processors
    fun onEntityInteractionEvent(entity: Entity) {
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
                entity[creature]!!.health = 3   //todo initial health from template
            } else {
                entity[creature]!!.health--

                val isDead = entity[creature]!!.health == 0
                val animationId = if (isDead) AnimationType.Death else AnimationType.TakeHit

                EventBus.send(Event.EntityAnimationStartEvent(entity, animationId))
            }
        }
    }
}
