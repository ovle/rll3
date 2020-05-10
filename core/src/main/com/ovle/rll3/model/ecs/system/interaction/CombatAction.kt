package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity

data class CombatAction(
    val name: String,
    val timeCost: Int = 100,
    val damageMod: Int = 0,
    val damageTakenMod: Int = 0,
    val hitMade: Boolean = true,
    val hitTaken: Boolean = true,
    val staminaCost: Int = 0,
    val effectDelay: Int = 0,
    val special: ((Entity) -> Unit)? = null
)