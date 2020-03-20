package com.ovle.rll3.view.sprite.animation

import com.ovle.rll3.model.ecs.component.AnimationType

//todo use script / text config
val torchAnimation = AnimationTemplate(
    type = AnimationType.Idle,
    frames = arrayOf(
        7 to 1,
        8 to 1,
        9 to 1,
        10 to 1
    ),
    repeat = true,
    alwaysPlaying = true
)
val scelAnimationIdle = AnimationTemplate(
    type = AnimationType.Idle,
    frames = arrayOf(
        7 to 10,
        8 to 10,
        9 to 10,
        10 to 10
    ),
    repeat = true,
    alwaysPlaying = true
)
val scelAnimationDeath = AnimationTemplate(
    type = AnimationType.Death,
    frames = arrayOf(
        7 to 12,
        8 to 12,
        9 to 12
    ),
    isTerminal = true
)
val scelAnimationDamaged = AnimationTemplate(
    type = AnimationType.TakeHit,
    frames = arrayOf(
        10 to 12,
        11 to 12
    )
)
val wizAnimationIdle = AnimationTemplate(
    type = AnimationType.Idle,
    frames = arrayOf(
        1 to 3,
        2 to 3,
        3 to 3,
        4 to 3
    ),
    repeat = true,
    alwaysPlaying = true
)
val wizAnimationWalk = AnimationTemplate(
    type = AnimationType.Walk,
    frames = arrayOf(
        0 to 1,
        1 to 1,
        2 to 1,
        3 to 1
    ),
    repeat = true
)