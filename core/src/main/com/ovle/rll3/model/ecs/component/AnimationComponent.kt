package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.view.sprite.animation.FrameAnimation

class AnimationComponent(
    var animations: Map<String, FrameAnimation> = mapOf()
) : Component {
    var currentAnimation: FrameAnimation? = null

    fun startAnimation(id: String) {
        currentAnimation?.let {
            stopAnimation(it.template.id)
        }

        currentAnimation = animations[id]
    }

    fun stopAnimations() {
        currentAnimation = null;

        animations.values.forEach { it.reset() }
    }

    fun stopAnimation(id: String) {
        val animationToStop = animations[id] ?: return
        if (animationToStop.template.isTerminal) return

        animationToStop.reset()
        if (animationToStop == currentAnimation) {
            currentAnimation = null;
        }
    }
}