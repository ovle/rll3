package com.ovle.rll3.model.module.health

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.Turn
import com.ovle.rll3.event.*
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.entity.livingEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get


class StaminaSystem : EventSystem() {

    override fun subscribe() {
        subscribe<TurnChangedEvent> { onTurnChangedEvent(it.turn) }
    }

    private fun onTurnChangedEvent(turn: Turn) {
        engine.livingEntities().forEach {
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