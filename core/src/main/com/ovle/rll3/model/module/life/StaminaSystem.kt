package com.ovle.rll3.model.module.life

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.Turn
import com.ovle.rll3.event.*
import com.ovle.rll3.model.util.livingEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.life.Components.life
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
        val life = entity[life]!!
        if (life.isDead || life.isStarved) return

        if (life.stamina < life.maxStamina) {
            life.stamina += 1
        }
    }
}