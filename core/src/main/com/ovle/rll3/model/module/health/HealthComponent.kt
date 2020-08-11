package com.ovle.rll3.model.module.health

import com.ovle.rll3.model.module.core.component.BaseComponent


class HealthComponent(
    var health: Int = 1,
    var maxHealth: Int = 1,
    var stamina: Int = 3,
    var maxStamina: Int = 3
) : BaseComponent {
    val isDead
        get() = health == 0
}