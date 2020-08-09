package com.ovle.rll3.model.module.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.Mappers
import ktx.ashley.get

fun staminaCost(source: Entity, cost: Int) {
    val livingComponent = source[Mappers.living]!!
    livingComponent.stamina -= cost
}