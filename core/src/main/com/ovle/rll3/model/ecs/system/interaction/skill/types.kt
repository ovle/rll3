package com.ovle.rll3.model.ecs.system.interaction.skill

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.system.time.Ticks

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

typealias SkillEffect = (Entity, Any?) -> Unit
typealias GetTarget = (GridPoint2, LevelInfo) -> Any?
typealias SkillCost = (Entity) -> Unit
