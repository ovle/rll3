package com.ovle.rll3.event

import com.ovle.rll3.event.Event.DebugSwitchControlMode
import com.ovle.rll3.event.Event.DebugSwitchSelectionMode
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.info

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
           "check task.. target: ${event.target.info()}"
        }
        is EntityTakeDamageEvent -> {
           "${event.entity.info()} takes ${event.amount} damage from ${event.source.info()}"
        }
//        is EntityUseSkillCommand -> {
//            "${event.source.info()} check skill ${event.skillTemplate.name} for use on ${event.target.info()}..."
//        }
        is EntityStartUseSkillEvent -> {
            " > ${event.source.info()} started skill ${event.skillTemplate.name} on ${event.target.info()}"
        }
        is EntityFinishUseSkillEvent -> {
            " < ${event.source.info()} finished skill ${event.skillTemplate.name} on ${event.target.info()} (amount: ${event.amount})"
        }
        is EntityDiedEvent -> { "${event.entity.info()} died" }
        else -> null
    }