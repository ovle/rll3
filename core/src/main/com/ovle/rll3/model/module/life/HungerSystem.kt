package com.ovle.rll3.model.module.life

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.Turn
import com.ovle.rll3.event.*
import com.ovle.rll3.model.module.core.entity.livingEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.life.Components.life
import ktx.ashley.get


class HungerSystem : EventSystem() {

    override fun subscribe() {
        subscribe<TurnChangedEvent> { onTurnChangedEvent(it.turn) }
        subscribe<EntityEatEvent> { onEntityEatEvent(it.entity, it.food) }
    }

    private fun onTurnChangedEvent(turn: Turn) {
        engine.livingEntities().forEach {
            processEntity(it)
        }
    }

    private fun processEntity(entity: Entity) {
        val life = entity[life]!!
        if (life.isDead || life.isStarved) return

        life.hunger += 1

        if (life.isStarved) {
            send(EntityStarvedEvent(entity))
        }
    }

    private fun onEntityEatEvent(entity: Entity, food: Entity) {
        val life = entity[life]!!
        //todo
        life.hunger = 0
//        life.hunger = max(life.hunger - food[resource]!!.amount, 0)

        send(DestroyEntityCommand(food))
    }
}