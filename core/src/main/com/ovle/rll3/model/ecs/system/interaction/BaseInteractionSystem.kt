package com.ovle.rll3.model.ecs.system.interaction

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.special.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.special.SelectionMode
import com.ovle.rll3.model.ecs.component.special.SelectionMode.Entity
import com.ovle.rll3.model.ecs.component.special.SelectionMode.Tile
import com.ovle.rll3.model.ecs.entity.playerInteractionInfo
import com.ovle.rll3.model.ecs.system.EventSystem


class BaseInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.DebugSwitchSelectionMode> { onSwitchSelectionModeEvent() }
    }

    private fun onSwitchSelectionModeEvent() {
        val interactionInfo = playerInteractionInfo()!!
        val newSelectionMode = if (interactionInfo.selectionMode == Entity) Tile else Entity
        switchSelectionMode(newSelectionMode, interactionInfo)
    }

    private fun switchSelectionMode(newSelectionMode: SelectionMode, interactionInfo: PlayerInteractionComponent) {
        interactionInfo.selectedEntity = null
        interactionInfo.selectedTiles = setOf()

        interactionInfo.selectionMode = newSelectionMode
    }
}
