package com.ovle.rll3.model.module.health

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.TimeChangedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.entity.livingEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get


class StaminaSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it.turn) }
    }

    private fun onTimeChangedEvent(turn: Turn) {
        livingEntities().forEach {
            processEntity(it)
        }
    }

    private fun processEntity(entity: Entity) {
        val health = entity[health]!!
        if (health.isDead || health.isStarved) return

        if (health.stamina < health.maxStamina) {
            health.stamina += 1
        }
    }
}