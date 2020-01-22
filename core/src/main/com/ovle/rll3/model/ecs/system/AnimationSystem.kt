package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.componentMapper
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.sprite.animation.animations
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import ktx.ashley.get


class AnimationSystem(
    private val spriteTexture: TexturesInfo
) : IteratingSystem(Family.all(AnimationComponent::class.java, RenderComponent::class.java).get()) {

    private val render: ComponentMapper<RenderComponent> = componentMapper()
    private val animation: ComponentMapper<AnimationComponent> = componentMapper()

    private val regions = TextureRegion.split(spriteTexture.texture, spriteWidth, spriteHeight)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        initAnimations(entity)

        //todo
    }

    //todo separate system ?
    private fun initAnimations(entity: Entity) {
        val animationComponent = entity[animation]
        if (animationComponent?.animations?.isEmpty() == true) {
            animationComponent.animations = animations(entity, regions).associateBy { it.template.id }
            val animation = animationComponent.animations.values.find { it.template.alwaysPlaying }
            animation?.let {
                animationComponent.startAnimation(it.template.id)
            }
        }
    }
}