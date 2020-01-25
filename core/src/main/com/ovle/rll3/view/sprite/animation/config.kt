package com.ovle.rll3.view.sprite.animation

//todo use script / text config
val torchAnimation = AnimationTemplate(
    id = "default",
    frameDuration = 0.125f,
    frames = arrayOf(
        0 to 4,
        1 to 4,
        2 to 4,
        3 to 4
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