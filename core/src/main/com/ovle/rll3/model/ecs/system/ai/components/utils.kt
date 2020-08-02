package com.ovle.rll3.model.ecs.system.ai.components

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo

fun entityQuery(query: String, entities: List<Entity>): Entity? {
    //todo by id
    //todo by template name
    return when (query) {
//        "player" -> playerInteractionInfo(entities)!!.controlledEntity
        else -> null
    }
}