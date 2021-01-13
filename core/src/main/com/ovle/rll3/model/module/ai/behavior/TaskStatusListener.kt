package com.ovle.rll3.model.module.ai.behavior

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
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
        if (task == root) {
            //todo cleanup?
            if (taskInfo != null) {
                when (status) {
                    SUCCEEDED -> send(TaskSucceedCommand(taskInfo))
                    FAILED -> send(TaskFailedCommand(taskInfo))
                    else -> { }
                }
            }
            if (isTerminalStatus) {
                send(BtFinishedEvent(tree))
            }
        }
    }
}