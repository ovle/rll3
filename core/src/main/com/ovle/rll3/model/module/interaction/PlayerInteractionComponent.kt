package com.ovle.rll3.model.module.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.BaseComponent
import com.ovle.rll3.model.module.skill.SkillTemplate

/**
 * @property focusedEntity      entity, which has the camera focus
 * @property hoveredEntity      entity under the cursor
 * @property selectedEntity     entity, which is currently selected by left-click
 * @property selectedSkill      skill of [selectedEntity], selected to be used
 * @property selectedSkillTarget     target(s) for [selectedSkill]
 */
class PlayerInteractionComponent(
    var focusedEntity: Entity? = null,
    var hoveredEntity: Entity? = null,

    //todo move to entity
    var selectedSkill: SkillTemplate? = null,
    var selectedSkillTarget: Any? = null,

    var selectedEntity: Entity? = null
) : BaseComponent