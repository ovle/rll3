package com.ovle.rll3.model.module.time

import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.event.*
import com.ovle.rll3.model.module.core.entity.timeInfo
import com.ovle.rll3.model.module.core.system.EventSystem


class TimeSystem : EventSystem() {

    override fun subscribe() {
        subscribe<IncGameSpeedCommand> { onIncGameSpeedCommand() }
        subscribe<DecGameSpeedCommand> { onDecGameSpeedCommand() }

        subscribe<SwitchPauseGameCommand> { onSwitchPauseGameCommand() }
    }

    private fun onIncGameSpeedCommand() {
        val time = engine.timeInfo()!!.time
        changeGameSpeed(time, 2.0)
    }

    private fun onDecGameSpeedCommand() {
        val time = engine.timeInfo()!!.time
        changeGameSpeed(time, 0.5)
    }

    private fun onSwitchPauseGameCommand() {
        val time = engine.timeInfo()!!.time
        time.paused = !time.paused
    }

    private fun changeGameSpeed(time: TimeInfo, multiplier: Double) {
        time.turnsInSecond *= multiplier
        send(GameSpeedChangedEvent(time.turnsInSecond))
    }

    override fun update(deltaTime: Float) {
        val time = engine.timeInfo()!!.time
        with(time) {
            if (paused) return

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