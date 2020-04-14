package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.ashley.core.Component

enum class Race(val value: String) {
    Human("human")
}

class LivingComponent(
    var health: Int = 1,
    var race: Race? = null
) : Component