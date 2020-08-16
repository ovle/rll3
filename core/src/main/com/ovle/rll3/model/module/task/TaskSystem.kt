package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Queue
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.CheckTaskCommand
import com.ovle.rll3.event.Event.GameEvent.TimeChangedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.taskPerformer
import com.ovle.rll3.model.module.core.entity.controlledEntities
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get

//todo how to use with behaviour trees
class TaskSystem : EventSystem() {

    private val taskQueue: Queue<TaskInfo> = Queue()

    override fun subscribe() {
        EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it.turn) }
        EventBus.subscribe<CheckTaskCommand> { onCheckTaskEvent(it.target) }
    }

    private fun onTimeChangedEvent(turn: Turn) {
        val controlledEntities = controlledEntities()
        controlledEntities
            .filter { it[taskPerformer] != null }
            .forEach {
                processFreePerformer(it)
            }

        controlledEntities
            .map { it[taskPerformer]!! }
            .filter { it.current != null }
            .forEach {
                processTaskPerformer(it)
            }
    }

    private fun onCheckTaskEvent(target: TaskTarget) {
        val taskTemplate = taskTemplates()
            .firstOrNull { it.targetFilter.invoke(target) } ?: return

        enqueueTask(taskTemplate, target)
    }

    private fun processFreePerformer(performer: Entity) {
        val freeTasks = taskQueue.filter { it.performer == null }
        val task = freeTasks.find { it.template.performerFilter.invoke(performer) } ?: return
        val preconditions = task.template.preconditions
        val failedPreconditions = preconditions.filterNot { it.condition.invoke(performer, task.target) }
        if (failedPreconditions.isEmpty()) {
            startTask(task, performer)
        } else {
            failedPreconditions.forEach {
                it.fulfillTask?.invoke(performer, task.target)
            }
        }
    }

    private fun startTask(task: TaskInfo, performer: Entity) {
        task.performer = performer
        performer[taskPerformer]!!.current = task
    }

    private fun processTaskPerformer(performerComponent: TaskPerformerComponent) {
        val task = performerComponent.current!!
        val (template, performer, target, started) = task
        checkNotNull(performer)

        if (!task.started) {
            template.oneTimeAction?.invoke(performer, target)
            task.started = true
            println("task started: $task")
        }

        if (template.successCondition.invoke(performer, target)) {
            //todo event
            performerComponent.current = null
            taskQueue.removeValue(task, true)
            println("task finished: $task")
            return
        }
//
//        template.failCondition?.let {
//            if (it.invoke(performer, target)) {
//                //todo event
//                performerComponent.current = null
//                println("task failed: $task")
//                return
//            }
//        }

        template.everyTurnAction?.invoke(performer, target)
    }

    private fun enqueueTask(taskTemplate: TaskTemplate, target: TaskTarget) {
        val task = TaskInfo(
            template = taskTemplate,
            performer = null,
            target = target
        )

        taskQueue.addFirst(task)
    }
}