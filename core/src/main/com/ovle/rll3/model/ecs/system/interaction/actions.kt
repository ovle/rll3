package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.*
import ktx.ashley.get
import ktx.ashley.has

val combatActionNames = combatActions.map { it.name }

fun defaultAction(entity: Entity) = actions(entity)
    .singleOrNull { it == defaultActionName(entity) }

private fun defaultActionName(entity: Entity): String {
    return when {
        (entity.has(levelConnection)) -> "travel"
        (entity.has(living)) -> "combat" //todo
        else -> "use"
    }
}

fun actions(entity: Entity)= values()
    .filter {
        it.check(entity)
    }.map {
        it.actionName
    }

fun isAvailable(action: String, source: Entity, target: Entity) =
    when (action) {
        Combat.actionName, Talk.actionName -> !target[living]!!.isDead
        else -> true
    }

fun isAvailable(action: CombatAction, source: Entity, target: Entity) =
    isEnoughStamina(action, source) &&
        !isRedundantWait(action, source)

private fun isRedundantWait(action: CombatAction, entity: Entity) =
    (action.name == "wait") && (entity[living]!!.stamina >= entity[living]!!.maxStamina)

private fun isEnoughStamina(action: CombatAction, entity: Entity) =
    action.staminaCost <= entity[living]!!.stamina