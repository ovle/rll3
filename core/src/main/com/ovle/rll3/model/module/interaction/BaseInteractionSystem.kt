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
        EventBus.subscribe<DebugSwitchSelectionMode> { onSwitchSelectionModeEvent() }
        EventBus.subscribe<DebugSwitchControlMode> { onSwitchControlModeEvent() }
    }

    private fun onSwitchSelectionModeEvent() {
        val interactionInfo = playerInteractionInfo()!!
        val newSelectionMode = if (interactionInfo.selectionMode == Entity) Tile else Entity
        switchSelectionMode(newSelectionMode, interactionInfo)
    }

    private fun onSwitchControlModeEvent() {
        val interactionInfo = playerInteractionInfo()!!
        val newControlMode = if (interactionInfo.controlMode == View) Task else View
        switchControlMode(newControlMode, interactionInfo)
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
