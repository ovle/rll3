package com.ovle.rll3.event

import com.ovle.rlUtil.event.Event
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rll3.model.module.space.position
import com.ovle.rll3.model.util.info

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
           "check task.. target: ${event.target.target.info()}"
        }
        is TaskStartedEvent -> {
           " > start task! performer: ${event.task.performer.info()} bt: ${event.task.template.btTemplate.name}"
        }
        is TaskFinishedEvent -> {
           " < finished task. performer: ${event.task.performer.info()}"
        }
        is EntityTakeDamageEvent -> {
           "${event.entity.info()} takes ${event.amount} damage from ${event.source.info()}"
        }
//        is EntityUseSkillCommand -> {
//            "${event.source.info()} check skill ${event.skillTemplate.name} for use on ${event.target.info()}..."
//        }
        is EntityStartUseSkillEvent -> {
            val (skill, source, target, payload) = event.info
            " > > ${source.info()} started skill ${skill.name} on ${target.info()}"
        }
        is EntityFinishUseSkillEvent -> {
            val (skill, source, target, payload) = event.info
            " < < ${source.info()} finished skill ${skill.name} on ${target.info()} (amount: ${event.amount})"
        }
        is EntityStarvedEvent -> { "${event.entity.info()} starved" }
        is EntityDiedEvent -> { "${event.entity.info()} died" }
        is EntityCarryItemEvent -> {
            "${event.entity.info()} carry ${event.item.info()}"
        }
        is EntityDropItemEvent -> {
            val position = event.entity.position().info()
            "${event.entity.info()} drop ${event.item.info()} on position $position"
        }
        else -> null
    }