package com.ovle.rll3.model.module.life

import com.ovle.rll3.model.module.core.component.EntityComponent


class LifeComponent(
    var health: Int = 1,
    var maxHealth: Int = 1,
    var stamina: Int = 3,
    var maxStamina: Int = 3,
    var hunger: Int = 0,
    var maxHunger: Int = 100
) : EntityComponent() {
    val isDead
        get() = health == 0
    val isExhausted
        get() = stamina == 0
    val isStarved
        get() = hunger == maxHunger
}