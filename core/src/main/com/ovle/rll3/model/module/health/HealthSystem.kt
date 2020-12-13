package com.ovle.rll3.model.module.health

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get
import kotlin.math.max

class HealthSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityTakeDamageEvent> { onEntityTakeDamageEvent(it.entity, it.source, it.amount) }
        EventBus.subscribe<EntityStarvedEvent> { onEntityStarvedEvent(it.entity) }
    }

    private fun onEntityTakeDamageEvent(entity: Entity, source: Entity?, amount: Int) {
        val health = entity[health]!!
        if (health.isDead) return

        health.health = max(health.health - amount, 0)

        if (health.health == 0) {
            EventBus.send(EntityDiedEvent(entity))
        }
    }

    private fun onEntityStarvedEvent(entity: Entity) {
        val health = entity[health]!!
        //todo
        health.health = 0

        EventBus.send(EntityDiedEvent(entity))
    }
}