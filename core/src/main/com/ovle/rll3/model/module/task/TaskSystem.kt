package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
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
import com.ovle.rll3.model.module.task.EntityConditions.isFreeTaskPerformer
import com.ovle.rll3.model.module.task.TaskStatus.*
import ktx.ashley.get


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
        EventBus.subscribe<TaskFailedCommand> { onTaskFailCommand(it.task) }

        EventBus.subscribe<EntityDiedEvent> { onEntityDiedEvent(it.entity) }
        EventBus.subscribe<CancelAllTasksCommand> { onCancelAllTasksCommand() }
    }

    private fun onTimeChangedEvent(turn: Turn) {
        validateTasks()

        val location = locationInfo()
        val controlledEntities = controlledEntities()
        controlledEntities
            .filter { isFreeTaskPerformer(it) }
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
        task.status = Finished
        cleanupTask(task)
    }

    private fun onTaskFailCommand(task: TaskInfo){
        task.status = Failed
        cleanupTask(task)
    }

    private fun onEntityDiedEvent(entity: Entity) {
        val performerComponent = entity[taskPerformer] ?: return
        val currentTask = performerComponent.current ?: return

        currentTask.status = Waiting
        cleanupPerformer(currentTask)

        currentTask.performer = null
    }

    private fun onCancelAllTasksCommand() {
        val tasksCopy = tasks().toList()
        tasksCopy.forEach {
            it.status = Cancelled
            cleanupTask(it)
        }
    }

    private fun processFreePerformer(performer: Entity, location: LocationInfo) {
        val freeTasks = tasks().filter { isFreeTask(it) }
        val task = freeTasks.find {
            it.template.performerFilter.invoke(performer, it.target, location)
        } ?: return

        startTask(task, performer)
    }

    private fun startTask(task: TaskInfo, performer: Entity) {
        task.performer = performer
        task.status = InProgress

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

    private fun validateTasks() {
        val tasksInfo = tasksInfo()!!
        val invalidTasks = tasksInfo.tasks.filter { !isValid(it) }
        invalidTasks.forEach {
            println("task $it removed (is no longer valid)")
            tasksInfo.tasks.removeValue(it, true)
        }
    }

    private fun isValid(taskInfo: TaskInfo?): Boolean {
        val isTargetValid = taskInfo?.target?.isValid() ?: true
        val isPerformerValid = true //todo?
        return isTargetValid && isPerformerValid
    }

    private fun cleanupTask(task: TaskInfo) {
        cleanupPerformer(task)

        tasks().removeValue(task, true)
        taskHistory().addLast(task)

        send(TaskFinishedEvent(task))
    }

    private fun cleanupPerformer(task: TaskInfo) {
        val performer = task.performer ?: return

        performer[taskPerformer]!!.current = null
    }

    private fun isFreeTask(it: TaskInfo) = it.status == Waiting
//    private fun isFreeTask(it: TaskInfo) = it.performer == null
    private fun tasks() = tasksInfo()!!.tasks
    private fun taskHistory() = tasksInfo()!!.taskHistory
}