package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.event.Event.GameEvent.TaskFailedCommand
import com.ovle.rll3.event.Event.GameEvent.TaskSucceedCommand
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.task.TaskInfo


class TaskStatusListener(
    private val tree: BehaviorTree<BTParams>,
    private val taskInfo: TaskInfo?
) : BehaviorTree.Listener<BTParams> {

    override fun childAdded(task: Task<BTParams>?, index: Int) {}

    override fun statusUpdated(task: Task<BTParams>, previousStatus: Task.Status?) {
        val status = task.status
        val isTerminalStatus = status in arrayOf(SUCCEEDED, FAILED)

        if (task is BaseTask && isTerminalStatus) {
            println("$status: ${task.name}")
        }

        val root = tree.getChild(0)
        if (task == root && taskInfo != null) {
            //todo cleanup?
            when (status) {
                SUCCEEDED -> EventBus.send(TaskSucceedCommand(taskInfo))
                FAILED -> EventBus.send(TaskFailedCommand(taskInfo))
                else -> { }
            }
        }
    }
}