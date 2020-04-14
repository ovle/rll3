package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.EntityInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import ktx.ashley.get


class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntityInteractionEvent> { onEntityInteractionEvent(it.entity, it.interaction) }
    }

    //todo rewrite to processors
    private fun onEntityInteractionEvent(entity: Entity, interaction: EntityInteraction) {
        when (interaction) {
            EntityInteraction.Travel -> {
                val playerEntity = playerInteractionInfo()!!.controlledEntity!!
                val connectionComponent = entity[levelConnection]!!

                send(Event.EntityLevelTransition(playerEntity, connectionComponent.id))
            }
        }

//        if (entity.has<DoorComponent>()) {
//            entity[door]!!.let { it.closed = !it.closed }
//            val closed =  entity[door]!!.closed
//
//            //todo shouldn't know about this here
//            entity[render]?.let { it.sprite = null }
//            entity[collision]?.let { it.active = closed }
//            //todo recalculate nearest lightning sources
//        }
//
//        //todo state machine?
//        if (entity.has<LivingComponent>()) {
//            if (entity[living]!!.health > 0) {
//                entity[living]!!.health--
//
//                EventBus.send(Event.EntityTakeDamage(entity, 1))
//
//                val isDead = entity[living]!!.health == 0
//                if (isDead) {
//                    EventBus.send(Event.EntityDied(entity))
//                }
//            }
//        }
    }
}
