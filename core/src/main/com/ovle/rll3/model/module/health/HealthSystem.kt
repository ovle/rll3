package com.ovle.rll3.model.module.health

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.KillEntityCommand
import com.ovle.rll3.event.Event.ResurrectEntityCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get
import ktx.ashley.has
import kotlin.math.max
import kotlin.math.min

class HealthSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityTakeDamageEvent> { onEntityTakeDamageEvent(it.entity, it.source, it.amount) }
        EventBus.subscribe<EntityStarvedEvent> { onEntityStarvedEvent(it.entity) }
        EventBus.subscribe<KillEntityCommand> { onKillSelectedEntityCommand(it.entity) }
        EventBus.subscribe<ResurrectEntityCommand> { onResurrectEntityCommand(it.entity) }
    }

    private fun onKillSelectedEntityCommand(entity: Entity) {
        if (!entity.has(health)) return

        applyDamage(entity, entity[health]!!.health)
    }

    private fun onResurrectEntityCommand(entity: Entity) {
        if (!entity.has(health)) return

        applyHeal(entity, entity[health]!!.maxHealth)
    }

    private fun onEntityTakeDamageEvent(entity: Entity, source: Entity?, amount: Int) {
        applyDamage(entity, amount)
    }

    private fun onEntityStarvedEvent(entity: Entity) {
        val health = entity[health]!!
        //todo
        health.health = 0

        send(EntityDiedEvent(entity))
    }


    private fun applyDamage(entity: Entity, amount: Int) {
        check(amount >= 0) { "damage amount is negative: $amount" }

        val health = entity[health]!!
        if (health.isDead) return

        health.health = max(health.health - amount, 0)

        if (health.health == 0) {
            send(EntityDiedEvent(entity))
        }
    }

    private fun applyHeal(entity: Entity, amount: Int) {
        check(amount >= 0) { "heal amount is negative: $amount" }

        val health = entity[health]!!
        val wasDead = health.isDead

        health.health = min(amount, health.maxHealth)

        if (wasDead) {
            send(EntityResurrectedEvent(entity))
        }
    }
}