package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.skill.CostStatus.*
import ktx.ashley.get

enum class CostStatus {
    Paid,
    NotPaid
}

fun staminaCost(source: Entity, cost: Int): CostStatus {
    val health = source[ComponentMappers.health]!!
    if (health.stamina < cost) return NotPaid

    health.stamina -= cost
    return Paid
}