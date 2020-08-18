package com.ovle.rll3.model.module.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.component.BaseComponent

/**
 * @property focusedEntity      entity, which has the camera focus
 * @property hoveredEntity      entity under the cursor
 * @property selectedEntity     entity, which is currently selected by left-click, to have some interaction with
 * @property selectedTiles      tiles, which is currently selected by left-click, to have some interaction with
 */
class PlayerInteractionComponent(
    var focusedEntity: Entity? = null,
    var hoveredEntity: Entity? = null,
    var selectedEntity: Entity? = null,
    var selectedTiles: Set<GridPoint2> = setOf(),
    var controlMode: ControlMode = ControlMode.View,
    var selectionMode: SelectionMode = SelectionMode.Entity
) : BaseComponent