package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.id
import ktx.ashley.get
import kotlin.math.max

class LivingSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntityTakeDamage> { onEntityTakeDamageEvent(it.entity, it.source, it.amount) }
    }

    private fun onEntityTakeDamageEvent(entity: Entity, source: Entity?, amount: Int) {
        val lc = entity[living]!!
        if (lc.health == 0) {
            println("${entity.id()} is already dead")
            return
        }

        lc.health = max(lc.health - amount, 0)

        if (lc.health == 0) {
            EventBus.send(Event.EntityDied(entity))
        }
    }
}