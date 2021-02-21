package com.ovle.rll3.model.module.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rll3.model.module.core.component.GlobalComponent
import com.ovle.rll3.model.module.game.AreaInfo

/**
 * @property focusedEntity      entity, which has the camera focus
 * @property hoveredEntity      entity under the cursor
 * @property selectedEntity     entity, which is currently selected by left-click, to have some interaction with
 * @property selectedTiles      tiles, which is currently selected by left-click, to have some interaction with
 */
class PlayerInteractionComponent(
    var gridPosition: GridPoint2 = point(0, 0),

    var focusedEntity: Entity? = null,
    var hoveredEntity: Entity? = null,
    var selectedEntity: Entity? = null,
    var selectedArea: AreaInfo? = null,

    var selectionRectangle: Rectangle? = null,
    var controlMode: ControlMode = ControlMode.View,
    var selectionMode: SelectionMode = SelectionMode.Entity
) : GlobalComponent()