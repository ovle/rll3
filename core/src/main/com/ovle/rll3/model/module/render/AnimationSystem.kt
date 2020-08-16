package com.ovle.rll3.model.module.render
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.event.Event.GameEvent.EntityFinishedMoveEvent
import com.ovle.rll3.event.Event.GameEvent.EntityStartMoveCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.render
import ktx.ashley.get


class AnimationSystem: IteratingSystem(Family.all(RenderComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderComponent = entity[render]!!
        val animation = renderComponent.currentAnimation
        animation?.let {
            it.time = it.time.plus(deltaTime)

            val type = it.type
            val config = animationConfig[type]!!
            val totalLength = config.totalLength

            if (it.time > totalLength) it.time = it.time % totalLength
        }
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        subscribe()
    }

    fun subscribe() {
        EventBus.subscribe<EntityStartMoveCommand> { onEntityStartMoveCommand(it.entity) }
        EventBus.subscribe<EntityFinishedMoveEvent> { onEntityFinishedMoveEvent(it.entity) }
    }

    private fun onEntityStartMoveCommand(entity: Entity) {
        entity[render]!!.currentAnimation = Animation(type = AnimationType.Walk)
    }

    private fun onEntityFinishedMoveEvent(entity: Entity) {
        entity[render]!!.currentAnimation = null
    }
}