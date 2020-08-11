package com.ovle.rll3.model.module.interaction

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.GameEvent.EntityEvent.*
import com.ovle.rll3.event.Event.PlayerControlEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.core.entity.playerInteractionInfo
import com.ovle.rll3.model.module.core.system.EventSystem

/**
 * player's interaction with entities (low-level)
 */
class EntityInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<EntityClickEvent> { onEntityClickEvent(it.entity, it.button) }
        EventBus.subscribe<ClickEvent> { onClickEvent(it.button, it.point) }
        EventBus.subscribe<VoidClickEvent> { onVoidClickEvent(it.button, it.point) }
        EventBus.subscribe<EntityHoverEvent> { onEntityHoverEvent(it.entity) }
        EventBus.subscribe<EntityUnhoverEvent> { onEntityUnhoverEvent() }

        EventBus.subscribe<EntityDestroyedEvent> { onEntityDestroyedEvent(it.entity) }
    }

    private fun onEntityDestroyedEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        with(interactionInfo) {
            if (selectedEntity == entity) selectedEntity = null
            if (focusedEntity == entity) focusedEntity = null
            if (hoveredEntity == entity) hoveredEntity = null
        }
    }

    private fun onEntityClickEvent(entity: Entity, button: Int) {
        val interactionInfo = playerInteractionInfo()!!
        if (interactionInfo.selectionMode == SelectionMode.Entity) {
            select(entity)
        }

        val needCheckTask = interactionInfo.controlMode == ControlMode.Task
        if (needCheckTask) {
            send(CheckTaskCommand(TaskTarget.EntityTarget(entity)))
        }
    }

    private fun onClickEvent(button: Int, point: GridPoint2) {
        val interactionInfo = playerInteractionInfo()!!
        val needCheckTask = interactionInfo.controlMode == ControlMode.Task
        if (needCheckTask) {
            send(CheckTaskCommand(TaskTarget.PositionTarget(point)))
        }
    }

    private fun onVoidClickEvent(button: Int, point: GridPoint2) {
        deselect()
        unfocus()
    }

    private fun onEntityHoverEvent(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = entity

        send(ShowEntityInfoCommand(entity))
    }

    private fun onEntityUnhoverEvent() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.hoveredEntity = null

        send(HideEntityInfoCommand())
    }

    private fun select(entity: Entity) {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.selectedEntity = entity
    }

    private fun deselect() {
        val interactionInfo = playerInteractionInfo()!!

        interactionInfo.selectedEntity = null
    }

    private fun unfocus() {
        val interactionInfo = playerInteractionInfo()!!
        interactionInfo.focusedEntity = null
    }
}