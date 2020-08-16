package com.ovle.rll3.model.module.render

import com.ovle.rll3.model.util.Direction

val animationConfig = mapOf(
    AnimationType.Walk to TransformAnimation.ShiftAnimation(
        direction = Direction.V,
        frames = arrayOf(0, 1, 2, 1),
        frameLength = 0.25f
    )
)