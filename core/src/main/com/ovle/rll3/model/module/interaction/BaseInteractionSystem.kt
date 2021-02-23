package com.ovle.rll3.model.module.interaction

import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.event.DebugSwitchControlMode
import com.ovle.rll3.event.DebugSwitchSelectionMode
import com.ovle.rll3.model.util.playerInteractionInfo
import com.ovle.rll3.model.module.core.system.EventSystem


class BaseInteractionSystem : EventSystem() {

    override fun subscribe() {
        subscribe<DebugSwitchSelectionMode> { onSwitchSelectionModeEvent(it.selectionMode) }
        subscribe<DebugSwitchControlMode> { onSwitchControlModeEvent(it.controlMode) }
    }

    private fun onSwitchSelectionModeEvent(selectionMode: SelectionMode) {
        val interactionInfo = engine.playerInteractionInfo()!!
        switchSelectionMode(selectionMode, interactionInfo)
    }

    private fun onSwitchControlModeEvent(controlMode: ControlMode) {
        val interactionInfo = engine.playerInteractionInfo()!!
        switchControlMode(controlMode, interactionInfo)
    }

    private fun switchSelectionMode(newSelectionMode: SelectionMode, interactionInfo: PlayerInteractionComponent) {
        interactionInfo.selectedEntity = null
        interactionInfo.selectionRectangle = null

        interactionInfo.selectionMode = newSelectionMode
    }

    private fun switchControlMode(newControlMode: ControlMode, interactionInfo: PlayerInteractionComponent) {
        interactionInfo.controlMode = newControlMode
    }
}
