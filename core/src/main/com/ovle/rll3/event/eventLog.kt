package com.ovle.rll3.event

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.template
import ktx.ashley.get
import ktx.ashley.has

fun eventLogHook(event: Event) {
    val message = message(event) ?: return

    println(message)
    send(LogCommand(message))
}

private fun message(event: Event) =
    when (event) {
        is DebugSwitchSelectionMode -> {
            "switch selection mode: ${event.selectionMode}"
        }
        is DebugSwitchControlMode -> {
            "switch control mode: ${event.controlMode}"
        }
        is CheckTaskCommand -> {
           "check task.. target: ${event.target}"
        }
//        is EntityInteraction -> {
//            val sourceInfo = event.source.info()
//            val entityInfo = event.entity.info()
//            val actionInfo = event.interaction
//            "$sourceInfo $actionInfo $entityInfo"
//        }
//        is EntityTakeDamageEvent -> {
//            val entityInfo = event.entity.info()
//            val sourceInfo = event.source?.info() ?: " unknown source"
//            "$entityInfo takes ${event.amount} damage from $sourceInfo"
//        }
        is EntityDiedEvent -> { "${event.entity.info()} died" }
//        is EntityTakeItems -> {
//            val itemsInfo = event.items.info()
//            "${event.entity.info()} taken: $itemsInfo"
//        }
//        is QuestStatusUpdatedEvent -> {
//            val quest = event.quest
//            "${quest.performer.info()} updated quest: ${quest.description.title}; status: ${quest.status}"
//        }
        else -> null
    }

fun Collection<Entity>.info(): String {
    val entityInfos = this.map { it.info() }
    return entityInfos.groupBy { it }.entries.joinToString(", ") { (k, v) -> "${v.count()}x $k" }
}

fun Entity.info() = when {
    has(template) -> this[template]!!.template.name
    else -> "(unknown)"
}