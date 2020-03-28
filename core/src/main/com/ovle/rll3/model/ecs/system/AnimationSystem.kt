package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.Mappers
import com.ovle.rll3.model.ecs.component.Mappers.animation
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.ecs.entity.entitiesWith
import com.ovle.rll3.model.template.AnimationType
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.sprite.animation.animations
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class AnimationSystem(
    spriteTexture: TexturesInfo
) : EventSystem() {

    private val regions = TextureRegion.split(spriteTexture.texture, spriteWidth, spriteHeight)

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        val entities = entitiesWith(allEntities().toList(), AnimationComponent::class)
        entities.forEach {
            processEntity(it)
        }
    }

    fun processEntity(entity: Entity) {
        val animationComponent = entity[Mappers.animation]!!
        val current = animationComponent.currentAnimation ?: return
        val template = current.template

        val isNeedStopCurrentAnimation = !template.repeat && !template.isTerminal && current.isFinished()
        if (isNeedStopCurrentAnimation){
            onEntityAnimationStop(entity, template.type)
        }
    }


    override fun subscribe() {
        subscribe<EntityInitialized> { onEntityInitialized(it.entity) }

        subscribe<EntityStartMove> { onEntityAnimationStart(it.entity, AnimationType.Walk) }
        subscribe<EntityFinishMove> { onEntityAnimationStop(it.entity, AnimationType.Walk) }
        subscribe<EntityTakeDamage> { onEntityAnimationStart(it.entity, AnimationType.TakeHit) }
        subscribe<EntityDied> { onEntityAnimationStart(it.entity, AnimationType.Death) }
    }

    private fun onEntityInitialized(entity: Entity) {
        initAnimations(entity)
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

    private fun stopAnimations(animation: AnimationComponent) {
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