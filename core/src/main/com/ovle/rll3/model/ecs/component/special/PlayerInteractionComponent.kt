package com.ovle.rll3.model.ecs.component.special

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate

/**
 * @property controlledEntity   entity, controlled by the player (affected by the user input events)
 * @property focusedEntity      entity, which has the camera focus
 * @property selectedEntity     entity, which is currently selected by left-click, to have some interaction
 * @property selectedTiles      tiles, which is currently selected by left-click, to have some interaction
 * @property hoveredEntity      entity under the cursor
 * @property selectedSkillTemplate      selected skill of the controlled entity
 */
class PlayerInteractionComponent(
    var controlledEntity: Entity? = null,
    var focusedEntity: Entity? = null,
    var selectedEntity: Entity? = null,
    var hoveredEntity: Entity? = null,
    var selectedTiles: Set<GridPoint2> = setOf(),
    var selectedSkillTemplate: SkillTemplate?,
    var selectionMode: SelectionMode = SelectionMode.Entity
) : BaseComponent

enum class SelectionMode {
    Entity,
    Tile
}