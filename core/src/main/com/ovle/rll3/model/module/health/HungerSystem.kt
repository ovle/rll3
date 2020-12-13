package com.ovle.rll3.model.module.health

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.entity.livingEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get
import kotlin.math.max


class HungerSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it.turn) }
        EventBus.subscribe<EntityEatEvent> { onEntityEatEvent(it.entity, it.amount) }
    }

    private fun onTimeChangedEvent(turn: Turn) {
        livingEntities().forEach {
            processEntity(it)
        }
    }

    private fun processEntity(entity: Entity) {
        val health = entity[health]!!
        if (health.isDead || health.isStarved) return

        health.hunger += 1

        if (health.isStarved) {
            EventBus.send(EntityStarvedEvent(entity))
        }
    }

    private fun onEntityEatEvent(entity: Entity, amount: Int) {
        val health = entity[health]!!
        health.hunger = max(amount, health.maxHunger)
    }
}