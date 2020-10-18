package com.ovle.rll3.model.module.game

import com.ovle.rll3.model.module.core.component.BaseComponent
import com.ovle.rll3.model.module.skill.SkillTemplate

class ActionComponent(
    var selectedSkill: SkillTemplate? = null,
    var selectedSkillTarget: Any? = null
) : BaseComponent