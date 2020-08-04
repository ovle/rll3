package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.dto.TaskTarget
import com.ovle.rll3.model.ecs.component.special.ControlMode
import com.ovle.rll3.model.ecs.component.special.SelectionMode
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem

//todo move technical stuff out?
/**
 * player's interaction with entities (low-level)
 */
class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityClick> { onEntityClickEvent(it.entity, it.button) }
        EventBus.subscribe<Click> { onClickEvent(it.button, it.point) }
        EventBus.subscribe<VoidClick> { onVoidClickEvent(it.button, it.point) }
        EventBus.subscribe<EntityHover> { onEntityHoverEvent(it.entity) }
        EventBus.subscribe<EntityUnhover> { onEntityUnhoverEvent() }

//        EventBus.subscribe<Event.EntityInteraction> { onEntityActionEvent(it.entity, it.interaction) }
    }

    private fun onEntityClickEvent(entity: Entity, button: Int) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.selectionMode == SelectionMode.Entity) {
            select(entity)
        }

        val needCheckTask = interactionInfo.controlMode == ControlMode.Task
        if (needCheckTask) {
            send(CheckTask(TaskTarget.EntityTarget(entity)))
        }
    }

    private fun onClickEvent(button: Int, point: GridPoint2) {
        val interactionInfo = playerInteractionInfo()!!
        val needCheckTask = interactionInfo.controlMode == ControlMode.Task
        if (needCheckTask) {
            send(CheckTask(TaskTarget.PositionTarget(point)))
        }
    }

    private fun onVoidClickEvent(button: Int, point: GridPoint2) {
        deselect()
        send(HideEntityActions())
    }


    private fun onEntityHoverEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = entity

        send(ShowEntityInfo(entity))
    }

    private fun onEntityUnhoverEvent() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = null

        send(HideEntityInfo())
    }

    private fun select(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = entity
    }

    //at least one party entity is already selected?
    private fun deselect() {
        val interactionInfo = playerInteractionInfo()!!

        interactionInfo.selectedEntity = null
    }

//    private fun onEntityActionEvent(entity: Entity, interaction: EntityInteraction) {
//        performEntityInteraction(entity, interaction)
//    }

//    private fun performEntityInteraction(entity: Entity, interaction: EntityInteraction) {
//        val playerEntity = controlledEntity()!!
//
//        when (interaction.type) {
//            Travel -> {
//                val connectionComponent = entity[levelConnection]!!
//                send(EntityLevelTransition(playerEntity, connectionComponent.id))
//            }
//            Talk -> showTalkActions(playerEntity, entity)
//            Use -> {
//                use(playerEntity, entity)
//                //todo events
//            }
//        }
//    }

}
