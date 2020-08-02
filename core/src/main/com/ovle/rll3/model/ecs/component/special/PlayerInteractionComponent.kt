package com.ovle.rll3.model.ecs.component.special

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.system.interaction.skill.SkillTemplate

/**
 * @property focusedEntity      entity, which has the camera focus
 * @property hoveredEntity      entity under the cursor
 * @property selectedEntity     entity, which is currently selected by left-click, to have some interaction
 * @property selectedTiles      tiles, which is currently selected by left-click, to have some interaction
 */
class PlayerInteractionComponent(
    var focusedEntity: Entity? = null,
    var hoveredEntity: Entity? = null,
    var selectedEntity: Entity? = null,
    var selectedTiles: Set<GridPoint2> = setOf(),
    var controlMode: ControlMode = ControlMode.View,
    var selectionMode: SelectionMode = SelectionMode.Entity
) : BaseComponent

enum class ControlMode {
    View,
    Task
}
enum class SelectionMode {
    Entity,
    Tile
}