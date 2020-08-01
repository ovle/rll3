package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.component.BaseComponent

enum class Race(val value: String) {
    Human("human")
}

class LivingComponent(
    var health: Int = 1,
    var maxHealth: Int = 1,
    var stamina: Int = 3,
    var maxStamina: Int = 3,
    var race: Race? = null
) : BaseComponent {
    val isDead
        get() = health == 0
}