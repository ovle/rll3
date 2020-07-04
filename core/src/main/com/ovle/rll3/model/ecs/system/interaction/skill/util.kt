package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import ktx.ashley.get

const val defaultSkill = "attack"

fun staminaCost(source: Entity, cost: Int) {
    val livingComponent = source[living]!!
    livingComponent.stamina -= cost
}