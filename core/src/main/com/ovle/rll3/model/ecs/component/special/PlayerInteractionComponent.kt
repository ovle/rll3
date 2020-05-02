package com.ovle.rll3.model.ecs.component.special

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

/**
 * @property controlledEntity   entity, controlled by the player (affected by the user input events)
 * @property focusedEntity      entity, which has the camera focus
 * @property selectedEntity     entity, which is currently selected by left-click, to have some interaction
 * @property hoveredEntities    entities under the cursor
 */
class PlayerInteractionComponent(
    var controlledEntity: Entity? = null,
    var focusedEntity: Entity? = null,
    var selectedEntity: Entity? = null,
    var hoveredEntities: Collection<Entity> = listOf()
) : Component