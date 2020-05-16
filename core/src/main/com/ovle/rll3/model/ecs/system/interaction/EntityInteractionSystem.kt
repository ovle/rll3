package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.entity.controlledEntity
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.entity.selectedEntity
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.*
import com.ovle.rll3.model.ecs.system.interaction.use.use
import ktx.ashley.get


class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityClick> { onEntityClickEvent(it.entity, it.button) }
        EventBus.subscribe<VoidClick> { onVoidClickEvent(it.button, it.point) }
        EventBus.subscribe<EntityHoverEvent> { onEntityHoverEvent(it.entity) }
        EventBus.subscribe<EntityUnhoverEvent> { onEntityUnhoverEvent() }

        EventBus.subscribe<EntityActionEvent> { onEntityInteractionEvent(it.entity, it.action) }
    }

    private fun onEntityClickEvent(entity: Entity, button: Int) {
        select(entity)

        when (button) {
            Buttons.RIGHT -> {
                showActions(entity)
            }
            else -> {
                val defaultAction = defaultAction(entity)
                if (defaultAction == null) {
                    showActions(entity)
                } else {
                    performEntityInteraction(entity, defaultAction)
                }
            }
        }
    }

    private fun onVoidClickEvent(button: Int, point: GridPoint2) {
        deselect()
        send(HideEntityActionsEvent())
    }

    private fun onEntityHoverEvent(entity: Entity) {
        if (selectedEntity() != null) return

        send(ShowEntityInfoEvent(entity))
    }

    private fun onEntityUnhoverEvent() {
        if (selectedEntity() != null) return

        send(HideEntityInfoEvent())
    }

    private fun select(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = entity
    }

    private fun deselect() {
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
                //todo event log waits for send(EntityActionEvent(playerEntity, entity, action))
                //but it causes endless recursion here
            }
        }
    }

    private fun showCombatActions(playerEntity: Entity, entity: Entity) {
        val combatActions = combatActions.filter { isAvailable(it, playerEntity, entity) }.map { it.name }
        send(ShowEntityActionsEvent(entity, combatActions))
    }
}
