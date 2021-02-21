package com.ovle.rll3.model.module.entityAction

import com.ovle.rll3.ExactTurn
import com.ovle.rll3.model.module.core.component.EntityComponent
import com.ovle.rll3.model.module.skill.SkillTemplate

/**
 * @property current       action (will be started when [timeLeft] expires)
 * @property skill         [SkillTemplate] that caused this action (not every action caused by skill)
 * @property animation     action animation (will be started immediately)
 * @property timeLeft      time for action to be performed
 */
class EntityActionComponent(
    var current: (() -> Unit)? = null,
    var skill: SkillTemplate? = null,
//    var animation: AnimationType? = null,
    var turnsLeft: ExactTurn? = null
) : EntityComponent()