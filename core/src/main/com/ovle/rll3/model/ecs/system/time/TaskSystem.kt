package com.ovle.rll3.model.ecs.system.time

import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.basic.TaskPerformerComponent
import com.ovle.rll3.model.ecs.component.dto.*
import com.ovle.rll3.model.ecs.component.util.Mappers.taskPerformer
import com.ovle.rll3.model.ecs.entity.controlledEntities
import com.ovle.rll3.model.ecs.system.EventSystem
import ktx.ashley.get

//todo how to use with behaviour trees
class TaskSystem : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.TimeChanged> { onTimeChangedEvent(it.turn) }
        EventBus.subscribe<Event.CheckTask> { onCheckTaskEvent(it.target) }
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

    private fun onCheckTaskEvent(target: TaskTarget) {
        when (target) {
            is TaskTarget.PositionTarget -> startTask(positionTargetTemplate(), target)
            is TaskTarget.EntityTarget -> startTask(entityTargetTemplate(), target)
        }
    }

    private fun entityTargetTemplate() = attackTaskTemplate
    private fun positionTargetTemplate() = moveToTaskTemplate

    private fun process(performerComponent: TaskPerformerComponent) {
        val task = performerComponent.current!!
        val (template, performer, target, started) = task

        if (!task.started) {
            template.oneTimeAction?.invoke(performer, target)
            task.started = true
            println("task started: $task")
        }

        if (template.successCondition.invoke(performer, target)) {
            //todo event
            performerComponent.current = null
            println("task finished: $task")
            return
        }

        template.failCondition?.let {
            if (it.invoke(performer, target)) {
                //todo event
                performerComponent.current = null
                println("task failed: $task")
                return
            }
        }

        template.everyTurnAction?.invoke(performer, target)
    }

    private fun startTask(taskTemplate: TaskTemplate, target: TaskTarget) {
        val controlledEntities = controlledEntities()
            .filter { taskTemplate.performerFilter.invoke(it) }
        controlledEntities.forEach {
            val performerComponent = it[taskPerformer]!!
            performerComponent.current = TaskInfo(
                template = taskTemplate,
                performer = it,
                target = target
            )
        }
    }
}