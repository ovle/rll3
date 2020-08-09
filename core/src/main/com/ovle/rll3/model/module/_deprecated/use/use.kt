package com.ovle.rll3.model.module._deprecated.use

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module._deprecated.DoorComponent
import com.ovle.rll3.model.module._deprecated.StashComponent
import com.ovle.rll3.model.module.core.component.has

class EntityInteractionCase(
    val condition: (Entity, Entity) -> Boolean,
    val action: (Entity, Entity) -> Unit
)

val entityInteractionCases = arrayOf(
    EntityInteractionCase(
        condition = { _, e: Entity -> e.has<DoorComponent>() },
        action = { s, e: Entity -> processDoor(s, e) }
    ),
    EntityInteractionCase(
        condition = { _, e: Entity -> e.has<StashComponent>() },
        action = { s, e: Entity -> processStash(s, e) }
    )
)

fun use(source: Entity, entity: Entity) {
    val case = entityInteractionCases.find { it.condition.invoke(source, entity) }
    case?.action?.invoke(source, entity)
}