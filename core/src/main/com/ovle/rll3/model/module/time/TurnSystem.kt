package com.ovle.rll3.model.module.time

import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.entity.gameInfo
import com.ovle.rll3.model.module.core.system.EventSystem


class TurnSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<PlayerFinishedTurnEvent> { onPlayerFinishedTurnEvent() }
        EventBus.subscribe<PlayerFinishedTurnEvent> { onAIFinishedTurnEvent() }
        EventBus.subscribe<TurnAppliedEvent> { onTurnAppliedEvent() }
    }

    private fun onPlayerFinishedTurnEvent() {
        val game = gameInfo()!!
        with(game) {
            turn.status = TurnStatus.WaitAI;
            EventBus.send(AITurnCommand())
        }
    }

    private fun onAIFinishedTurnEvent() {
        val game = gameInfo()!!
        with(game) {
            turn.status = TurnStatus.Apply;
            EventBus.send(ApplyTurnCommand())
        }
    }

    private fun onTurnAppliedEvent() {
        val game = gameInfo()!!
        with(game) {
            turn.turn++
            turn.status = TurnStatus.WaitPlayer;
            EventBus.send(TurnFinishedEvent())
        }
    }
}