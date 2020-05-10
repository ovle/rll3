package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.special.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.Combat
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.Travel
import ktx.ashley.get


class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntitySelectEvent> { onEntitySelectEvent(it.entity) }
        EventBus.subscribe<EntityDeselectEvent> { onEntityDeselectEvent() }
        EventBus.subscribe<EntityActionEvent> { onEntityInteractionEvent(it.entity, it.action) }
    }

    private fun onEntitySelectEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        if (!checkInteraction(entity, interactionInfo)) return

        interactionInfo.selectedEntity = entity

        val playerEntity = interactionInfo.controlledEntity!!

        val actions = actions(entity).filter { isAvailable(it, playerEntity, entity) }
        if (actions.isNotEmpty()) {
            send(ShowEntityActionsEvent(entity, actions))
        }
        send(ShowEntityInfoEvent(entity))
    }

    //todo duplicated in RenderObjectsSystem
    private fun checkInteraction(entity: Entity, interactionInfo: PlayerInteractionComponent): Boolean {
        val controlledEntity = interactionInfo.controlledEntity
        val fov = controlledEntity?.get(Mappers.perception)?.fov ?: return true

        val positionComponent = entity[Mappers.position]!!
        return positionComponent.gridPosition in fov
    }

    private fun onEntityDeselectEvent() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = null
    }

    //todo rewrite to processors
    private fun onEntityInteractionEvent(entity: Entity, action: String) {
        val playerEntity = playerInteractionInfo()!!.controlledEntity!!

        when (action) {
            Travel.actionName -> {
                val connectionComponent = entity[levelConnection]!!
                send(EntityLevelTransition(playerEntity, connectionComponent.id))
            }
            Combat.actionName -> {
                showCombatActions(playerEntity, entity)
            }
        }

//        if (entity.has<DoorComponent>()) {
//            entity[door]!!.let { it.closed = !it.closed }
//            val closed =  entity[door]!!.closed
//
//            //todo shouldn't know about this here
//            entity[render]?.let { it.sprite = null }
//            entity[collision]?.let { it.active = closed }
//            //todo recalculate nearest lightning sources
//        }
//
    }

    private fun showCombatActions(playerEntity: Entity, entity: Entity) {
        val combatActions = combatActions.filter { isAvailable(it, playerEntity, entity) }.map { it.name }
        send(ShowEntityActionsEvent(entity, combatActions))
    }
}
