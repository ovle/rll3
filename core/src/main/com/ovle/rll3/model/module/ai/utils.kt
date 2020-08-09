package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.Entity

fun entityQuery(query: String, entities: List<Entity>): Entity? {
    //todo by id
    //todo by template name
    return when (query) {
//        "player" -> playerInteractionInfo(entities)!!.controlledEntity
        else -> null
    }
}