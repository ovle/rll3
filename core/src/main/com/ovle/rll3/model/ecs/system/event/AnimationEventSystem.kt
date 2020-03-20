package com.ovle.rll3.model.ecs.system.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.Event
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.AnimationType
import com.ovle.rll3.model.ecs.component.Mappers.animation
import com.ovle.rll3.model.ecs.component.has
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.sprite.animation.animations
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class AnimationEventSystem(
    spriteTexture: TexturesInfo
) : EventSystem<Event>() {

    private val regions = TextureRegion.split(spriteTexture.texture, spriteWidth, spriteHeight)


    override fun channel() = EventBus.receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is Event.LevelLoaded -> onLevelLoaded(event.level.objects + event.playerEntity)
            is Event.EntityAnimationStartEvent -> onEntityAnimationStart(event.entity, event.type)
            is Event.EntityAnimationStopEvent -> onEntityAnimationStop(event.entity, event.type)
        }
    }

    private fun onLevelLoaded(entities: Collection<Entity>) {
        entities.filter { it.has<AnimationComponent>() }
            .forEach {
                initAnimations(it)
            }
    }

    private fun onEntityAnimationStart(entity: Entity, type: AnimationType) {
        initAnimations(entity)

        val animation = entity[animation]
        animation?.let {
            startAnimation(it, type)
        }
    }

    private fun onEntityAnimationStop(entity: Entity, type: AnimationType) {
        val animation = entity[animation]
        animation?.let {
            val isTerminal = it.currentAnimation?.template?.isTerminal ?: false
            stopAnimation(it, type)
            if (!isTerminal) {
                startDefault(it)
            }
        }
    }

    private fun initAnimations(entity: Entity) {
        val animation = entity[animation] ?: return

        if (animation.animations.isEmpty()) {
            animation.animations = animations(entity, regions).associateBy { it.template.type }
            startDefault(animation)
        }
    }

    private fun startDefault(animation: AnimationComponent) {
        val defaultAnimation = animation.animations.values.find { it.template.alwaysPlaying }
        defaultAnimation?.let {
            startAnimation(animation, it.template.type)
        }
    }

    private fun startAnimation(animation: AnimationComponent, type: AnimationType) {
        animation.currentAnimation?.let {
            val template = it.template

            if (template.isTerminal) it.reset()
            else stopAnimation(animation, template.type)
        }

        animation.currentAnimation = animation.animations[type]
    }

    fun stopAnimations(animation: AnimationComponent) {
        animation.currentAnimation = null;
        animation.animations.values.forEach { it.reset() }
    }

    private fun stopAnimation(animation: AnimationComponent, type: AnimationType) {
        val animationToStop = animation.animations[type] ?: return
        if (animationToStop.template.isTerminal) return

        animationToStop.reset()
        if (animationToStop == animation.currentAnimation) {
            animation.currentAnimation = null;
        }
    }
}