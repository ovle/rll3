package com.ovle.rll3.view.sprite.animation

//todo use script / text config
val torchAnimation = AnimationTemplate(
    id = "default",
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
    id = "idle",
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
    id = "death",
    frames = arrayOf(
        7 to 12,
        8 to 12,
        9 to 12
    ),
    isTerminal = true
)
val scelAnimationDamaged = AnimationTemplate(
    id = "damaged",
    frames = arrayOf(
        10 to 12,
        11 to 12
    )
)
val scelAnimationResurrect = AnimationTemplate(
    id = "resurrect",
    frames = arrayOf(
        9 to 12,
        8 to 12,
        7 to 12
    )
)
val wizAnimationIdle = AnimationTemplate(
    id = "idle",
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
    id = "walk",
    frames = arrayOf(
        0 to 1,
        1 to 1,
        2 to 1,
        3 to 1
    ),
    repeat = true
)
val wizAnimationHit = AnimationTemplate(
    id = "hit",
    frames = arrayOf(
        0 to 2,
        1 to 2
    ),
    repeat = false
)