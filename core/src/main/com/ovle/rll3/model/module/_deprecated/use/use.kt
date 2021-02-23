package com.ovle.rll3.model.module._deprecated.use

import com.badlogic.ashley.core.Entity
import ktx.ashley.has

//class EntityInteractionCase(
//    val condition: (Entity, Entity) -> Boolean,
//    val action: (Entity, Entity) -> Unit
//)
//
//val entityInteractionCases = arrayOf(
//    EntityInteractionCase(
//        condition = { _, e: Entity -> e.has(door) },
//        action = { s, e: Entity -> processDoor(s, e) }
//    ),
//    EntityInteractionCase(
//        condition = { _, e: Entity -> e.has(stash) },
//        action = { s, e: Entity -> processStash(s, e) }
//    )
//)
//
//fun use(source: Entity, entity: Entity) {
//    val case = entityInteractionCases.find { it.condition.invoke(source, entity) }
//    case?.action?.invoke(source, entity)
//}