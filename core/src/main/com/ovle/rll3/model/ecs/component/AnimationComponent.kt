package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.view.sprite.animation.FrameAnimation

class AnimationComponent(
    var animations: Map<String, FrameAnimation> = mapOf()
) : Component {
    var current: FrameAnimation? = null

    fun startAnimation(id: String) {
        current = animations[id]
        current?.start()
    }

    fun stopAnimation(id: String) {
        val animationToStop = animations[id]
        animationToStop?.stop()
        if (animationToStop == current) {
            current = null;
        }
    }
}