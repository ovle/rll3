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
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get

//todo how to use with behaviour trees
class TaskSystem : EventSystem() {

    private val tasks: Queue<TaskInfo> = Queue()

    override fun subscribe() {
        EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it.turn) }

        EventBus.subscribe<CheckTaskCommand> { onCheckTaskCommand(it.target) }
        EventBus.subscribe<TaskSucceedCommand> { onTaskSucceedCommand(it.task) }
        EventBus.subscribe<TaskFailCommand> { onTaskFailCommand(it.task) }
    }

    private fun onTimeChangedEvent(turn: Turn) {
        val controlledEntities = controlledEntities()
        controlledEntities
            .filter { it[taskPerformer] != null }
            .forEach {
                processFreePerformer(it)
            }
    }

    private fun onCheckTaskCommand(target: TaskTarget) {
        val locationInfo = locationInfo()
        val taskTemplate = taskTemplates()
            .firstOrNull { it.targetFilter?.invoke(target, locationInfo) ?: true } ?: return

        enqueueTask(taskTemplate, target)
    }

    private fun onTaskSucceedCommand(task: TaskInfo) {
        cleanupTask(task)
    }

    private fun onTaskFailCommand(task: TaskInfo){
        cleanupTask(task)
    }

    private fun processFreePerformer(performer: Entity) {
        val freeTasks = tasks.filter { it.performer == null }
        val task = freeTasks.find {
            it.template.performerFilter.invoke(performer)
        } ?: return

        startTask(task, performer)
    }

    private fun startTask(task: TaskInfo, performer: Entity) {
        task.performer = performer
        performer[taskPerformer]!!.current = task

        send(TaskStartedEvent(task))
    }

    private fun enqueueTask(taskTemplate: TaskTemplate, target: TaskTarget) {
        val task = TaskInfo(
            template = taskTemplate,
            performer = null,
            target = target
        )

        //older task = more priority
        tasks.addLast(task)
//        tasks.addFirst(task)

        println("enqueueTask: $task")
    }

    private fun cleanupTask(task: TaskInfo) {
        val performer = task.performer!!
        performer[taskPerformer]!!.current = null
//        task.performer = null

        tasks.removeValue(task, true)

        send(TaskFinishedEvent(task))
    }
}