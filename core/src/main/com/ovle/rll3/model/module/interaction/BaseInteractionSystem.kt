package com.ovle.rll3.model.module.interaction

import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.interaction.ControlMode.*
import com.ovle.rll3.model.module.interaction.SelectionMode.Entity
import com.ovle.rll3.model.module.interaction.SelectionMode.Tile
import com.ovle.rll3.model.module.core.entity.playerInteractionInfo
import com.ovle.rll3.model.module.core.system.EventSystem


class BaseInteractionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<DebugSwitchSelectionMode> { onSwitchSelectionModeEvent(it.selectionMode) }
        EventBus.subscribe<DebugSwitchControlMode> { onSwitchControlModeEvent(it.controlMode) }
    }

    private fun onSwitchSelectionModeEvent(selectionMode: SelectionMode) {
        val interactionInfo = playerInteractionInfo()!!
        switchSelectionMode(selectionMode, interactionInfo)
    }

    private fun onSwitchControlModeEvent(controlMode: ControlMode) {
        val interactionInfo = playerInteractionInfo()!!
        switchControlMode(controlMode, interactionInfo)
    }

    private fun switchSelectionMode(newSelectionMode: SelectionMode, interactionInfo: PlayerInteractionComponent) {
        interactionInfo.selectedEntity = null
        interactionInfo.selectedTiles = setOf()

        interactionInfo.selectionMode = newSelectionMode
    }

    private fun switchControlMode(newControlMode: ControlMode, interactionInfo: PlayerInteractionComponent) {
        interactionInfo.controlMode = newControlMode
    }
}
