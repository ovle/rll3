package com.ovle.rll3.model.ecs.component.special

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

/**
 * @property controlledEntity   entity, controlled by the player (affected by the user input events)
 * @property focusedEntity      entity, which has the camera focus
 * @property hoveredEntities    entities under the cursor
 */
class PlayerInteractionComponent(
    val controlledEntity: Entity? = null,
    val focusedEntity: Entity? = null,
    var hoveredEntities: Collection<Entity> = listOf()
) : Component