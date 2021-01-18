package com.ovle.rll3.model.ai.bt

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.model.module.ai.behavior.BTParams
import com.ovle.rll3.model.module.ai.behavior.BaseTask
import com.ovle.rll3.model.module.ai.behavior.TaskExecResult
import com.ovle.rll3.model.module.ai.behavior.TaskStatusListener

class TestTaskStatusListener(
    tree: BehaviorTree<BTParams>,
    private val actualResult: MutableCollection<StepResult>
): TaskStatusListener(tree, null) {

    override fun statusUpdated(task: Task<BTParams>, previousStatus: Task.Status?) {
        super.statusUpdated(task, previousStatus)

        if (task is BaseTask) {
            actualResult += StepResult(task.name ?: "", TaskExecResult(task.status, null))
        }
    }
}