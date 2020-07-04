package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity

//todo target template, for player to be able to target
// empty place/self or simply click

data class SkillTemplate(
    val name: String = "",
    val cost: SkillCost = {},
    val time: Int = 0,
    val skill: Skill = {_, _ -> }
)

typealias Skill = (Entity, Any?) -> Unit
typealias SkillCost = (Entity) -> Unit
