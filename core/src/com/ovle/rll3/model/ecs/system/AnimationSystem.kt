package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.componentMapper
import ktx.ashley.get


class AnimationSystem : IteratingSystem(Family.all(AnimationComponent::class.java, RenderComponent::class.java).get()) {

    private val animatedMapper: ComponentMapper<AnimationComponent> = componentMapper()
    private val renderMapper: ComponentMapper<RenderComponent> = componentMapper()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animationComponent = entity[animatedMapper]!!
        val renderComponent = entity[renderMapper]!!

        animationComponent.time += deltaTime
        //todo
//        renderComponent.sprite?.sprite = animationComponent.animation.getKeyFrame(animationComponent.time)
    }

}