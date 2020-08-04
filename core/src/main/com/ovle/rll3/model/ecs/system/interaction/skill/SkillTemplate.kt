package com.ovle.rll3.model.ecs.system.interaction.skill

import com.ovle.rll3.*

/**
 * @property name
 * @property cost
 * @property target
 * @property time
 * @property skillEffect
 */
data class SkillTemplate(
    val name: String = "",
    val cost: SkillCost = {},
    val target: GetTarget? = null,
    val time: Ticks = 0,
    val skillEffect: SkillEffect = { _, _, _ -> },
    val skillEffectAmount: GetEffectAmount = { _ -> 1 }
)
