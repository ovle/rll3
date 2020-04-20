package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.*
import ktx.ashley.get

val combatActionNames = combatActions.map { it.name }

fun actions(entity: Entity)= values()
    .filter {
        it.check(entity)
    }.map {
        it.actionName
    }

fun isAvailable(action: String, source: Entity, target: Entity) =
    when (action) {
        Combat.actionName, Talk.actionName -> !target[Mappers.living]!!.isDead
        else -> true
    }

fun isAvailable(action: CombatAction, source: Entity, target: Entity) =
    isEnoughStamina(action, source) &&
        !isRedundantWait(action, source)

private fun isRedundantWait(action: CombatAction, entity: Entity) =
    (action.name == "wait") && (entity[Mappers.living]!!.stamina >= entity[Mappers.living]!!.maxStamina)

private fun isEnoughStamina(action: CombatAction, entity: Entity) =
    action.staminaCost <= entity[Mappers.living]!!.stamina