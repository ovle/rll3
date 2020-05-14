package com.ovle.rll3.model.ecs.system.interaction.use

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.advanced.DoorComponent
import com.ovle.rll3.model.ecs.component.util.has

class EntityInteractionCase(
    val condition: (Entity, Entity) -> Boolean,
    val action: (Entity, Entity) -> Unit
)

val entityInteractionCases = arrayOf(
    EntityInteractionCase(
        condition = { _, e: Entity -> e.has<DoorComponent>() },
        action = { s, e: Entity -> processDoor(s, e) }
    )
)

fun use(source: Entity, entity: Entity) {
    val case = entityInteractionCases.find { it.condition.invoke(source, entity) }
    case?.action?.invoke(source, entity)
}