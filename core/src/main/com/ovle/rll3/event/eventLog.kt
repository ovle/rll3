package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.model.ecs.component.basic.TemplateComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.component.util.has
import ktx.ashley.get

fun eventLogHook(event: Event) {
    if (!isLoggableEvent(event)) return

    println(message(event))
}

private fun isLoggableEvent(event: Event) =
    when (event) {
//        is EntityInteractionEvent,
        is EntityTakeDamage,
        is EntityDied,
        is EntityLevelTransition -> true
        else -> false
    }

private fun message(event: Event)=
    when (event) {
//        is EntityInteractionEvent,
        is EntityTakeDamage -> {
            val entityInfo = event.entity.info()
            val sourceInfo = event.source?.info() ?: " unknown source"
            "$entityInfo takes ${event.amount} of <type> damage from $sourceInfo"
        }
        is EntityDied -> { "${event.entity.info()} died" }
        is EntityLevelTransition -> {
            val entityInfo = event.entity.info()
            "$entityInfo moved to another place"
        }
        else -> throw IllegalArgumentException("not supported journal for event $event")
    }

private fun Entity.info() = when {
    has<TemplateComponent>() -> this[template]!!.template.name
    else -> "(unknown)"
}