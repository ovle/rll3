package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.ComponentMappers
import ktx.ashley.get

fun staminaCost(source: Entity, cost: Int) {
    val livingComponent = source[ComponentMappers.health]!!
    livingComponent.stamina -= cost
}