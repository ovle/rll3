package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.view.sprite.animation.FrameAnimation

enum class AnimationType {
    Idle,
    Walk,
    TakeHit,
    Death
}

class AnimationComponent(
    var animations: Map<AnimationType, FrameAnimation> = mapOf()
) : Component {
    var currentAnimation: FrameAnimation? = null
}