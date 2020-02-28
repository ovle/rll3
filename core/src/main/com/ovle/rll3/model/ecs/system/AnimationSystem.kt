package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.Event
import com.ovle.rll3.EventBus
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.componentMapper
import com.ovle.rll3.model.ecs.component.has
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.sprite.animation.animations
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class AnimationSystem(
    spriteTexture: TexturesInfo
) : EventSystem<Event>() {

    private val animation: ComponentMapper<AnimationComponent> = componentMapper()

    private val regions = TextureRegion.split(spriteTexture.texture, spriteWidth, spriteHeight)


    override fun channel() = EventBus.receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is Event.LevelLoaded -> onLevelLoaded(event.level.objects)
            is Event.EntityAnimationStartEvent -> onEntityAnimationStart(event.entity, event.animationId)
            is Event.EntityAnimationStopEvent -> onEntityAnimationStop(event.entity, event.animationId)
        }
    }

    private fun onLevelLoaded(entities: Collection<Entity>) {
        entities.filter { it.has(AnimationComponent::class) }
            .forEach {
                initAnimations(it)
            }
    }

    private fun onEntityAnimationStart(entity: Entity, animationId: String) {
        initAnimations(entity)

        val animationComponent = entity[animation]
        animationComponent?.startAnimation(animationId)
    }

    private fun onEntityAnimationStop(entity: Entity, animationId: String) {
        val animationComponent = entity[animation]
        animationComponent?.stopAnimation(animationId)
    }

    private fun initAnimations(entity: Entity) {
        val animationComponent = entity[animation] ?: return

        if (animationComponent.animations.isEmpty()) {
            animationComponent.animations = animations(entity, regions).associateBy { it.template.id }
            val animation = animationComponent.animations.values.find { it.template.alwaysPlaying }
            animation?.let {
                animationComponent.startAnimation(it.template.id)
            }
        }
    }
}