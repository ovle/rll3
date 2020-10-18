package com.ovle.rll3.model.module.game

import com.ovle.rll3.event.Event.ApplyTurnCommand
import com.ovle.rll3.event.Event.TurnAppliedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.system.EventSystem


class CombatSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<ApplyTurnCommand> { onApplyTurnCommand() }
    }

    private fun onApplyTurnCommand() {
        EventBus.send(TurnAppliedEvent())
    }
}