package com.ovle.rll3.model.module.time

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.core.component.ComponentMappers.game
import ktx.ashley.get


class TimeSystem : IteratingSystem(all(GameComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val gameComponent = entity[game]!!
        with(gameComponent.time) {
            fractionTicks += deltaTicks(deltaTime)

            if (fractionTicks >= ticksInTurn) {
                val deltaTurns = fractionTicks / ticksInTurn
                turn += deltaTurns
                fractionTicks -= deltaTurns * ticksInTurn

                send(TimeChangedEvent(turn))
//                println("$turn . $fractionTicks")
            }
        }
    }
}