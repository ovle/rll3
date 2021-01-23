package com.ovle.rll3.model.module.entityAction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.ExactTurn
import com.ovle.rll3.event.Event.GameEvent.TimeChangedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.entityAction
import com.ovle.rll3.model.module.core.entity.actionEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get


class EntityActionSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it) }
    }

    private fun onTimeChangedEvent(event: TimeChangedEvent) {
        val entities = actionEntities()
        entities.forEach { processEntity(it, event.exactDeltaTurns) }
    }

    private fun processEntity(entity: Entity, exactDeltaTurns: ExactTurn) {
        val actionComponent = entity[entityAction]!!
        with(actionComponent) {
            if (current == null) return

            turnsLeft = turnsLeft!!.minus(exactDeltaTurns)

            if (turnsLeft!! <= 0) {
                current!!.invoke()
                current = null
                turnsLeft = null
            }
        }
    }
}