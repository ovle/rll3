package com.ovle.rll3.model.module.skill.dto

import com.ovle.rll3.*
import com.ovle.rll3.model.module.skill.CostStatus

/**
 * @property name
 * @property cost
 * @property target
 * @property turns
 * @property effect
 */
data class SkillTemplate(
    val name: String = "",
    val cost: SkillCost = { CostStatus.Paid },
    val target: GetTarget? = null,
    val turns: Turn = 0,
    val effect: SkillEffect = { _, _ -> },
    val effectAmount: GetEffectAmount = { _ -> 1 },
    val isSuccess: SkillSuccessCondition = { _, _, _ -> true }
)
