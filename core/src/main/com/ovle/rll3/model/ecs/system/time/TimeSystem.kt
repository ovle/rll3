package com.ovle.rll3.model.ecs.system.time

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.special.WorldComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.world
import ktx.ashley.get


class TimeSystem : IteratingSystem(all(WorldComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val worldComponent = entity[world]!!
        with(worldComponent.time) {
            fractionTicks += deltaTicks(deltaTime)
            if (fractionTicks >= ticksInTurn) {
                val deltaTurns = fractionTicks / ticksInTurn
                turn += deltaTurns
                fractionTicks -= deltaTurns * ticksInTurn

                send(Event.TimeChanged(turn))
//                println("$turn . $fractionTicks")
            }
        }
    }
}