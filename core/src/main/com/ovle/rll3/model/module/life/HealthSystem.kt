package com.ovle.rll3.model.module.life

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.event.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.life.Components.life
import ktx.ashley.get
import ktx.ashley.has
import kotlin.math.max
import kotlin.math.min

class HealthSystem : EventSystem() {

    override fun subscribe() {
        subscribe<EntityTakeDamageEvent> { onEntityTakeDamageEvent(it.entity, it.source, it.amount) }
        subscribe<EntityStarvedEvent> { onEntityStarvedEvent(it.entity) }
        subscribe<KillEntityCommand> { onKillSelectedEntityCommand(it.entity) }
        subscribe<ResurrectEntityCommand> { onResurrectEntityCommand(it.entity) }
    }

    private fun onKillSelectedEntityCommand(entity: Entity) {
        if (!entity.has(life)) return

        applyDamage(entity, entity[life]!!.health)
    }

    private fun onResurrectEntityCommand(entity: Entity) {
        if (!entity.has(life)) return

        applyHeal(entity, entity[life]!!.maxHealth)
    }

    private fun onEntityTakeDamageEvent(entity: Entity, source: Entity?, amount: Int) {
        applyDamage(entity, amount)
    }

    private fun onEntityStarvedEvent(entity: Entity) {
        val life = entity[life]!!
        //todo
        life.health = 0

        send(EntityDiedEvent(entity))
    }


    private fun applyDamage(entity: Entity, amount: Int) {
        check(amount >= 0) { "damage amount is negative: $amount" }

        val life = entity[life]!!
        if (life.isDead) return

        life.health = max(life.health - amount, 0)

        if (life.health == 0) {
            send(EntityDiedEvent(entity))
        }
    }

    private fun applyHeal(entity: Entity, amount: Int) {
        check(amount >= 0) { "heal amount is negative: $amount" }

        val life = entity[life]!!
        val wasDead = life.isDead

        life.health = min(amount, life.maxHealth)

        if (wasDead) {
            send(EntityResurrectedEvent(entity))
        }
    }
}