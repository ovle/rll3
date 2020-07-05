package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get

fun staminaCost(source: Entity, cost: Int) {
    val livingComponent = source[Mappers.living]!!
    livingComponent.stamina -= cost
}