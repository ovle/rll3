package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.ashley.core.Component

//todo rewrite?
class ActionComponent(
    var current: (() -> Unit)? = null,
    var timeLeft: Int? = null
) : Component