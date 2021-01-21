package com.ovle.rll3.model.ai.bt

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.model.module.ai.behavior.*


class TestTaskStatusListener(
    tree: BehaviorTree<BTParams>,
    private val actualResult: MutableCollection<StepResult>
): TaskStatusListener(tree, null) {

    override fun statusUpdated(task: Task<BTParams>, previousStatus: Task.Status?) {
        super.statusUpdated(task, previousStatus)

        if (task is BaseTask) {
            val status = task.status
            if (!isTerminal(status)) return

            actualResult += StepResult(task.name ?: "", TaskExecResult(status, null))
        }
    }
}