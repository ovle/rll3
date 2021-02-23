package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.Turn
import com.ovle.rll3.event.*
import com.ovle.rll3.model.util.controlledEntities
import com.ovle.rll3.model.util.locationInfo
import com.ovle.rll3.model.util.tasksInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.dto.LocationInfo
import com.ovle.rll3.model.module.task.Components.taskPerformer
import com.ovle.rll3.model.module.task.dto.TaskInfo
import com.ovle.rll3.model.util.conditions.EntityConditions.isFreeTaskPerformer
import com.ovle.rll3.model.module.task.dto.TaskStatus.*
import com.ovle.rll3.model.module.task.dto.TaskTarget
import com.ovle.rll3.model.module.task.dto.TaskTemplate
import ktx.ashley.get


class TaskSystem : EventSystem() {

    private val isRealTime = false

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (isRealTime) {
            onTurnChangedEvent(0)
        }
    }

    override fun subscribe() {
        if (!isRealTime) {
            subscribe<TurnChangedEvent> { onTurnChangedEvent(it.turn) }
        }

        subscribe<CheckTaskCommand> { onCheckTaskCommand(it.target) }
        subscribe<TaskSucceedCommand> { onTaskSucceedCommand(it.task) }
        subscribe<TaskFailedCommand> { onTaskFailCommand(it.task) }

        subscribe<CancelAllTasksCommand> { onCancelAllTasksCommand() }
    }


    private fun onTurnChangedEvent(turn: Turn) {
        validateTasks()

        val location = engine.locationInfo()!!
        val controlledEntities = engine.controlledEntities()
        controlledEntities
            .filter { isFreeTaskPerformer(it) }
            .forEach {
                processFreePerformer(it, location)
            }
    }

    private fun onCheckTaskCommand(target: TaskTarget) {
        val locationInfo = engine.locationInfo()!!
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

    private fun onCancelAllTasksCommand() {
        val tasksCopy = tasks().toList()
        tasksCopy.forEach {
            it.status = Cancelled
            cleanupTask(it)
        }
    }


    private fun resetTask(currentTask: TaskInfo) {
        currentTask.status = Waiting
        cleanupPerformer(currentTask)

        currentTask.performer = null
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

    //todo remove duplicates (same target+template? what priority?)
    private fun validateTasks() {
        val tasksInfo = engine.tasksInfo()!!

        val invalidTasks = tasksInfo.tasks.filter { !isValid(it) }
        invalidTasks.forEach {
            println("task $it removed (is no longer valid)")
            tasksInfo.tasks.removeValue(it, true)
        }

        val tasksToReset = tasksInfo.tasks.filter {
            it.performer != null && !isValidPerformer(it.performer!!)
        }
        tasksToReset.forEach {
            println("task $it reset (no valid performer)")
            resetTask(it)
        }
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

    private fun tasks() = engine.tasksInfo()!!.tasks

    private fun taskHistory() = engine.tasksInfo()!!.taskHistory
}