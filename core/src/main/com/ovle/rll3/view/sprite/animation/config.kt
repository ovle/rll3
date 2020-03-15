package com.ovle.rll3.view.sprite.animation

//todo use script / text config
val torchAnimation = AnimationTemplate(
    id = "default",
    frameDuration = 0.125f,
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
    frameDuration = 0.125f,
    frames = arrayOf(
        7 to 10,
        8 to 10,
        9 to 10,
        10 to 10
    ),
    repeat = true,
    alwaysPlaying = true
)
val wizAnimationIdle = AnimationTemplate(
    id = "idle",
    frameDuration = 0.125f,
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
    frameDuration = 0.125f,
    frames = arrayOf(
        1 to 1,
        2 to 1,
        3 to 1,
        4 to 1
    ),
    repeat = true
)
val wizAnimationHit = AnimationTemplate(
    id = "hit",
    frameDuration = 0.25f,
    frames = arrayOf(
        0 to 2,
        1 to 2
    ),
    repeat = false
)