package com.ovle.rll3.model.module.skill

import com.ovle.rll3.*

/**
 * @property name
 * @property cost
 * @property target
 * @property turns
 * @property skillEffect
 */
data class SkillTemplate(
    val name: String = "",
    val cost: SkillCost = {},
    val target: GetTarget? = null,
    val turns: Turn = 0,    //todo rewrite skill processing to support multi-turn actions
    val skillEffect: SkillEffect = { _, _, _ -> },
    val skillSuccess: SkillSuccessCondition = { _, _, _ -> true },
    val skillEffectAmount: GetEffectAmount = { _ -> 1 }
)
