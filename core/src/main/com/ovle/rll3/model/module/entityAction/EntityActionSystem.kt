package com.ovle.rll3.model.module.entityAction

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family.all
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.module.core.component.Mappers.action
import com.ovle.rll3.model.module.time.deltaTicks
import ktx.ashley.get


class EntityActionSystem : IteratingSystem(all(EntityActionComponent::class.java).get()) {

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