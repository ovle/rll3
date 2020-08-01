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
    send(Log(message))
}

private fun isLoggableEvent(event: Event) =
    when (event) {
        is DebugShowInventory,

        is EntityInteraction,
        is EntityTakeDamage,
        is EntityDied,
        is EntityTakeItems,
        is QuestStatusUpdated,
        is EntityLevelTransition -> true
        else -> false
    }

private fun message(event: Event)=
    when (event) {
        is DebugShowInventory -> {
            val entityInfo = event.entity.info()
            val itemsInfo = event.items.info()
            "$entityInfo has items: $itemsInfo"
        }
        is EntityInteraction -> {
            val sourceInfo = event.source.info()
            val entityInfo = event.entity.info()
            val actionInfo = event.interaction
            "$sourceInfo $actionInfo $entityInfo"
        }
        is EntityTakeDamage -> {
            val entityInfo = event.entity.info()
            val sourceInfo = event.source?.info() ?: " unknown source"
            "$entityInfo takes ${event.amount}(b${event.blockedAmount}) damage from $sourceInfo"
        }
        is EntityDied -> { "${event.entity.info()} died" }
        is EntityTakeItems -> {
            val itemsInfo = event.items.info()
            "${event.entity.info()} taken: $itemsInfo"
        }
        is QuestStatusUpdated -> {
            val quest = event.quest
            "${quest.performer.info()} updated quest: ${quest.description.title}; status: ${quest.status}"
        }
        is EntityLevelTransition -> {
            val entityInfo = event.entity.info()
            "$entityInfo has leaved this level"
        }
        else -> throw IllegalArgumentException("not supported journal for event $event")
    }

fun Collection<Entity>.info(): String {
    val entityInfos = this.map { it.info() }
    return entityInfos.groupBy { it }.entries.joinToString(", ") { (k, v) -> "${v.count()}x $k" }
}

fun Entity.info() = when {
    has<TemplateComponent>() -> this[template]!!.template.name
    else -> "(unknown)"
}