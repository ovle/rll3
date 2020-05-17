package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.basic.TemplateComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.component.util.has
import ktx.ashley.get

fun eventLogHook(event: Event) {
    if (!isLoggableEvent(event)) return

    val message = message(event)
    println(message)
    send(LogEvent(message))
}

private fun isLoggableEvent(event: Event) =
    when (event) {
        is EntityActionEvent,
        is EntityCombatAction,
        is EntityTakeDamage,
        is EntityDied,
        is EntityTakeItems,
        is EntityLevelTransition -> true
        else -> false
    }

private fun message(event: Event)=
    when (event) {
        is EntityActionEvent -> {
            val sourceInfo = event.source.info()
            val entityInfo = event.entity.info()
            val actionInfo = event.action
            "$sourceInfo $actionInfo $entityInfo"
        }
        is EntityCombatAction -> {
            val entityInfo = event.entity.info()
            val actionInfo = event.action.name
            "$entityInfo $actionInfo"
        }
        is EntityTakeDamage -> {
            val entityInfo = event.entity.info()
            val sourceInfo = event.source?.info() ?: " unknown source"
            "$entityInfo takes ${event.amount}(b${event.blockedAmount}) damage from $sourceInfo"
        }
        is EntityDied -> { "${event.entity.info()} died" }
        is EntityTakeItems -> {
            val itemsInfo = event.items.map { it[template]?.template?.name ?: "unknown" }
            "${event.entity.info()} taken: $itemsInfo"
        }
        is EntityLevelTransition -> {
            val entityInfo = event.entity.info()
            "$entityInfo has leaved this level"
        }
        else -> throw IllegalArgumentException("not supported journal for event $event")
    }

private fun Entity.info() = when {
    has<TemplateComponent>() -> this[template]!!.template.name
    else -> "(unknown)"
}