package com.ovle.rll3.model.ecs.system.time

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.advanced.ActionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.action
import ktx.ashley.get


class ActionSystem : IteratingSystem(all(ActionComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val actionComponent = entity[action]!!
        with(actionComponent) {
            if (current == null) return
            checkNotNull(timeLeft)

//            if (animation != null) {
//                send(Event.EntityAnimationStart(entity, animation!!, timeLeft!!))
//                animation = null
//            }

            val deltaTicks = deltaTicks(deltaTime)
            timeLeft = timeLeft?.minus(deltaTicks)

            if (timeLeft!! <= 0) {
                current!!.invoke()
                current = null
                timeLeft = null
            }
        }
    }
}