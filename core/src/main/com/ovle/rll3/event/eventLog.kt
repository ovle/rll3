package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.template
import ktx.ashley.get
import ktx.ashley.has

fun eventLogHook(event: Event) {
    if (!isLoggableEvent(event)) return

    val message = message(event)
    println(message)
    send(GameEvent.LogCommand(message))
}

private fun isLoggableEvent(event: Event) =
    when (event) {
        is GameEvent.EntityEvent.EntityInteraction,
        is GameEvent.EntityEvent.EntityTakeDamageEvent,
        is GameEvent.EntityEvent.EntityDiedEvent,
        is GameEvent.EntityEvent.EntityTakeItems,
        is GameEvent.QuestStatusUpdatedEvent -> true
        else -> false
    }

private fun message(event: Event)=
    when (event) {
        is GameEvent.EntityEvent.EntityInteraction -> {
            val sourceInfo = event.source.info()
            val entityInfo = event.entity.info()
            val actionInfo = event.interaction
            "$sourceInfo $actionInfo $entityInfo"
        }
        is GameEvent.EntityEvent.EntityTakeDamageEvent -> {
            val entityInfo = event.entity.info()
            val sourceInfo = event.source?.info() ?: " unknown source"
            "$entityInfo takes ${event.amount} damage from $sourceInfo"
        }
        is GameEvent.EntityEvent.EntityDiedEvent -> { "${event.entity.info()} died" }
        is GameEvent.EntityEvent.EntityTakeItems -> {
            val itemsInfo = event.items.info()
            "${event.entity.info()} taken: $itemsInfo"
        }
        is GameEvent.QuestStatusUpdatedEvent -> {
            val quest = event.quest
            "${quest.performer.info()} updated quest: ${quest.description.title}; status: ${quest.status}"
        }
        else -> throw IllegalArgumentException("not supported journal for event $event")
    }

fun Collection<Entity>.info(): String {
    val entityInfos = this.map { it.info() }
    return entityInfos.groupBy { it }.entries.joinToString(", ") { (k, v) -> "${v.count()}x $k" }
}

fun Entity.info() = when {
    has(template) -> this[template]!!.template.name
    else -> "(unknown)"
}