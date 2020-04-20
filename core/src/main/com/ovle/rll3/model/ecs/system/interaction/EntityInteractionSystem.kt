package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.Combat
import com.ovle.rll3.model.ecs.system.interaction.EntityInteraction.Travel
import ktx.ashley.get


class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.EntitySelectEvent> { onEntitySelectEvent(it.entity) }
        EventBus.subscribe<Event.EntityActionEvent> { onEntityInteractionEvent(it.entity, it.action) }
    }

    private fun onEntitySelectEvent(entity: Entity) {
        val playerEntity = playerInteractionInfo()!!.controlledEntity!!
        send(Event.ShowEntityActionsEvent(entity, actions(entity).filter { isAvailable(it, playerEntity, entity) }))
    }

    //todo rewrite to processors
    private fun onEntityInteractionEvent(entity: Entity, action: String) {
        val playerEntity = playerInteractionInfo()!!.controlledEntity!!

        when (action) {
            Travel.actionName -> {
                val connectionComponent = entity[levelConnection]!!
                send(Event.EntityLevelTransition(playerEntity, connectionComponent.id))
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
        send(Event.ShowEntityActionsEvent(entity, combatActions))
    }
}
