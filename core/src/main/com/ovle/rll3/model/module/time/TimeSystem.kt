package com.ovle.rll3.model.module.time

import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.module.core.entity.gameInfo
import com.ovle.rll3.model.module.core.system.EventSystem


class TimeSystem : EventSystem() {

    override fun subscribe() {
        subscribe<IncGameSpeedCommand> { onIncGameSpeedCommand() }
        subscribe<DecGameSpeedCommand> { onDecGameSpeedCommand() }
    }

    private fun onIncGameSpeedCommand() {
        val gameComponent = gameInfo()!!
        changeGameSpeed(gameComponent.time, 2.0)
    }

    private fun onDecGameSpeedCommand() {
        val gameComponent = gameInfo()!!
        changeGameSpeed(gameComponent.time, 0.5)
    }

    private fun changeGameSpeed(time: TimeInfo, multiplier: Double) {
        time.turnsInSecond *= multiplier
        send(GameSpeedChangedEvent(time.turnsInSecond))
    }

    override fun update(deltaTime: Float) {
        val gameComponent = gameInfo()!!
        with(gameComponent.time) {
            val exactDeltaTurns = deltaTime * turnsInSecond
            val lastTurn = turn.toLong()
            turn += exactDeltaTurns
            val newTurn = turn.toLong()

            if (newTurn != lastTurn) {
                val deltaTurns = newTurn - lastTurn
                send(TurnChangedEvent(newTurn, deltaTurns))
            }

            send(TimeChangedEvent(exactDeltaTurns))
//            println("$turn (dt=$deltaTime)")
        }
    }
}