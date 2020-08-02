package com.ovle.rll3.model.ecs.system.time

import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.basic.TaskPerformerComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.taskPerformer
import com.ovle.rll3.model.ecs.entity.controlledEntities
import com.ovle.rll3.model.ecs.system.EventSystem
import ktx.ashley.get


class TaskSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.TimeChanged> { onTimeChangedEvent(it.turn) }
    }

    private fun onTimeChangedEvent(turn: Turn) {
        val controlledEntities = controlledEntities()

        controlledEntities
            .map { it[taskPerformer]!! }
            .filter { it.current != null }
            .forEach {
                process(it)
            }
    }

    private fun process(performerComponent: TaskPerformerComponent) {
        val task = performerComponent.current!!
        val performer = task.performer
        val target = task.target
        val template = task.template

        if (!task.started) {
            template.oneTimeAction?.invoke(performer, target)
            task.started = true
        }

        if (template.successCondition.invoke(performer, target)) {
            //todo event
            performerComponent.current = null
            println("task finished")
            return
        }

        template.everyTurnAction?.invoke(performer, target)
    }
}