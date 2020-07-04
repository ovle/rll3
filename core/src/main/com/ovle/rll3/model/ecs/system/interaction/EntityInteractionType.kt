package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.util.Mappers.container
import com.ovle.rll3.model.ecs.component.util.Mappers.door
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.component.util.Mappers.questOwner
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionType.*
import ktx.ashley.has


data class EntityInteraction(val type: EntityInteractionType, val payload: Any? = null)

enum class EntityInteractionType(
    val actionName: String
) {
    Investigate("investigate"),
    Skill("skill"),
    Talk("talk"),
    Use("use"),
    Travel("travel");
}

typealias EntityCheck = Entity.() -> Boolean
data class InteractionRule(val interactionType: EntityInteractionType, val check: EntityCheck)

private val interactionRules = listOf(
    InteractionRule(Talk) { has(questOwner) }, //todo dialog instead
    InteractionRule(Travel) { has(levelConnection) },
    InteractionRule(Use) { has(door) || has(container) },
    InteractionRule(Skill) { has(living) }, //todo base on current skill's target template?
    InteractionRule(Investigate) { true }
)

fun defaultInteraction(entity: Entity): EntityInteractionType? = availableInteractions(entity).firstOrNull()

fun availableInteractions(entity: Entity) =
    interactionRules.filter { it.check.invoke(entity) }.map { it.interactionType }
