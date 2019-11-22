package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.ecs.component.AnimationComponent
import com.ovle.rll3.model.ecs.component.RenderComponent
import com.ovle.rll3.model.ecs.get
import ktx.ashley.get


class AnimationSystem : IteratingSystem(Family.all(AnimationComponent::class.java, RenderComponent::class.java).get()) {

    private val animatedMapper: ComponentMapper<AnimationComponent> = get()
    private val renderMapper: ComponentMapper<RenderComponent> = get()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animated = entity[animatedMapper]
        val render = entity[renderMapper]

        //todo
//        render.texture = animated.animation.getKeyFrame(state.time)
    }

}