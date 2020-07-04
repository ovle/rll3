package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.advanced.DoorComponent
import com.ovle.rll3.model.ecs.component.advanced.LivingComponent
import com.ovle.rll3.model.ecs.component.basic.ContainerComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.container
import com.ovle.rll3.model.ecs.component.util.Mappers.door
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.component.util.has
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType.*
import ktx.ashley.has


data class EntityInteraction(val type: EntityInteractionType, val payload: Any? = null)

enum class EntityInteractionType(
    val actionName: String
) {
    Investigate("investigate"),
    Combat("combat"),
    Talk("talk"),
    Use("use"),
    Travel("travel");
}

typealias EntityCheck = Entity.() -> Boolean
data class InteractionRule(val interactionType: EntityInteractionType, val check: EntityCheck)

private val interactionRules = listOf(
//    InteractionRule(Combat) { has(living) }, //todo
    InteractionRule(Talk) { has(living) },
    InteractionRule(Use) { has(door) || has(container) },
    InteractionRule(Travel) { has(levelConnection) },
    InteractionRule(Investigate) { true }
)

fun defaultInteraction(entity: Entity): EntityInteractionType? = availableInteractions(entity).firstOrNull()

fun availableInteractions(entity: Entity) =
    interactionRules.filter { it.check.invoke(entity) }.map { it.interactionType }


//fun isAvailable(action: String, source: Entity, target: Entity) =
//    when (action) {
//        Combat.actionName, Talk.actionName -> !target[living]!!.isDead
//        else -> true
//    }
//
//fun isAvailable(action: CombatAction, source: Entity, target: Entity) =
//    isEnoughStamina(action, source) &&
//        !isRedundantWait(action, source)
//
//private fun isRedundantWait(action: CombatAction, entity: Entity) =
//    (action.name == "wait") && (entity[living]!!.stamina >= entity[living]!!.maxStamina)
//
//private fun isEnoughStamina(action: CombatAction, entity: Entity) =
//    action.staminaCost <= entity[living]!!.stamina