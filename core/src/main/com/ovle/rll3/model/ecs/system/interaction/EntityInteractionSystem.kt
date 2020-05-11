package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.entity.controlledEntity
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.see
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.*
import com.ovle.rll3.model.ecs.system.interaction.use.use
import ktx.ashley.get


class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntitySelectEvent> { onEntitySelectEvent(it.entity) }
        EventBus.subscribe<EntityDeselectEvent> { onEntityDeselectEvent() }

        EventBus.subscribe<EntityLeftClick> { onEntityLeftClickEvent(it.entity) }
        EventBus.subscribe<EntityRightClick> { onEntityRightClickEvent(it.entity) }

        EventBus.subscribe<EntityActionEvent> { onEntityInteractionEvent(it.entity, it.action) }
    }

    private fun onEntityLeftClickEvent(entity: Entity) {
        val defaultAction = defaultAction(entity)

        if (defaultAction == null) {
            showActions(entity)
        } else {
            performEntityInteraction(entity, defaultAction)
        }
    }

    private fun onEntityRightClickEvent(entity: Entity) {
        showActions(entity)
    }

    private fun onEntitySelectEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        val controlledEntity = interactionInfo.controlledEntity
        if (controlledEntity?.see(entity) == false) return

        interactionInfo.selectedEntity = entity

        send(ShowEntityInfoEvent(entity))
    }

    private fun onEntityDeselectEvent() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = null
    }

    private fun onEntityInteractionEvent(entity: Entity, action: String) {
        performEntityInteraction(entity, action)
    }


    private fun showActions(entity: Entity) {
        val controlledEntity = controlledEntity()
        val actions = actions(entity).filter { isAvailable(it, controlledEntity!!, entity) }
        if (actions.isNotEmpty()) {
            send(ShowEntityActionsEvent(entity, actions))
        }
    }

    private fun performEntityInteraction(entity: Entity, action: String) {
        val playerEntity = playerInteractionInfo()!!.controlledEntity!!

        when (action) {
            Travel.actionName -> {
                val connectionComponent = entity[levelConnection]!!
                send(EntityLevelTransition(playerEntity, connectionComponent.id))
            }
            Combat.actionName -> {
                showCombatActions(playerEntity, entity)
            }
            Use.actionName -> {
                use(playerEntity, entity)
            }
        }
    }

    private fun showCombatActions(playerEntity: Entity, entity: Entity) {
        val combatActions = combatActions.filter { isAvailable(it, playerEntity, entity) }.map { it.name }
        send(ShowEntityActionsEvent(entity, combatActions))
    }
}
