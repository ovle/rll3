package com.ovle.rll3.model.ecs.system.interaction.skill

import com.ovle.rll3.GetTarget
import com.ovle.rll3.SkillCost
import com.ovle.rll3.SkillEffect
import com.ovle.rll3.Ticks

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
    val skillEffect: SkillEffect = { _, _ -> }
)
