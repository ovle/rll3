package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Queue
import com.ovle.rll3.Turn
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.taskPerformer
import com.ovle.rll3.model.module.core.entity.controlledEntities
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.entity.tasksInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.LocationInfo
import ktx.ashley.get


//todo how to use with behaviour trees
class TaskSystem : EventSystem() {

    private val isRealTime = false


    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (isRealTime) {
            onTimeChangedEvent(0)
        }
    }

    override fun subscribe() {
        if (!isRealTime) {
            EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it.turn) }
        }

        EventBus.subscribe<CheckTaskCommand> { onCheckTaskCommand(it.target) }
        EventBus.subscribe<TaskSucceedCommand> { onTaskSucceedCommand(it.task) }
        EventBus.subscribe<TaskFailCommand> { onTaskFailCommand(it.task) }
    }

    private fun onTimeChangedEvent(turn: Turn) {
        val location = locationInfo()
        val controlledEntities = controlledEntities()
        controlledEntities
            .filter { isFreePerformer(it) }
            .forEach {
                processFreePerformer(it, location)
            }
    }

    private fun onCheckTaskCommand(target: TaskTarget) {
        val locationInfo = locationInfo()
        val taskTemplate = taskTemplates()
            .firstOrNull { it.targetFilter?.invoke(target, locationInfo) ?: true } ?: return

        enqueueTask(taskTemplate, target, locationInfo)
    }

    private fun onTaskSucceedCommand(task: TaskInfo) {
        cleanupTask(task)
    }

    private fun onTaskFailCommand(task: TaskInfo){
        cleanupTask(task)
    }

    private fun isFreePerformer(e: Entity): Boolean {
        val performerComponent = e[taskPerformer]
        return performerComponent != null && performerComponent.current == null
    }

    private fun processFreePerformer(performer: Entity, location: LocationInfo) {
        val freeTasks = tasks().filter { it.performer == null }
        val task = freeTasks.find {
            it.template.performerFilter.invoke(performer, it.target, location)
        } ?: return

        startTask(task, performer)
    }

    private fun startTask(task: TaskInfo, performer: Entity) {
        task.performer = performer

        val performerComponent = performer[taskPerformer]!!
        performerComponent.current = task

        send(TaskStartedEvent(task))
    }

    private fun enqueueTask(taskTemplate: TaskTemplate, target: TaskTarget, location: LocationInfo) {
        taskTemplate.targetMap.invoke(target, location).forEach {
            enqueueSingleTask(taskTemplate, it)
        }
    }

    private fun enqueueSingleTask(taskTemplate: TaskTemplate, target: TaskTarget) {
        val task = TaskInfo(
            template = taskTemplate,
            performer = null,
            target = target
        )

        //older task = more priority
        tasks().addLast(task)
//        tasks.addFirst(task)
        println("enqueueSingleTask: $task")
    }

    private fun cleanupTask(task: TaskInfo) {
        val performer = task.performer!!
        performer[taskPerformer]!!.current = null
//        task.performer = null

        tasks().removeValue(task, true)

        send(TaskFinishedEvent(task))
    }

    private fun tasks() = tasksInfo()!!.tasks
}