package com.ovle.rll3.model.module.skill.dto

import com.badlogic.ashley.core.Entity

data class SkillUsage(
    val skill: SkillTemplate,

    val source: Entity,
    val target: Any?,
    val payload: Any? = null
)