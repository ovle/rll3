package com.ovle.rll3.model.ecs.system.iterating

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.Mappers
import ktx.ashley.get


class AnimationSystem : IteratingSystem(Family.all(AnimationComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animationComponent = entity[Mappers.animation]!!
        val current = animationComponent.currentAnimation ?: return
        val template = current.template

        val isNeedStopCurrentAnimation = !template.repeat && !template.isTerminal && current.isFinished()
        if (isNeedStopCurrentAnimation){
            EventBus.send(Event.EntityAnimationStopEvent(entity, template.type))
        }
    }
}