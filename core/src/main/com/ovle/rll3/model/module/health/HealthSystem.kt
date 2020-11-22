package com.ovle.rll3.model.module.health

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.entity.id
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get
import kotlin.math.max

class HealthSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityTakeDamageEvent> { onEntityTakeDamageEvent(it.entity, it.source, it.amount) }
    }

    private fun onEntityTakeDamageEvent(entity: Entity, source: Entity?, amount: Int) {
        val lc = entity[health]!!
        if (lc.health == 0) return

        lc.health = max(lc.health - amount, 0)

        if (lc.health == 0) {
            EventBus.send(EntityDiedEvent(entity))
        }
    }
}