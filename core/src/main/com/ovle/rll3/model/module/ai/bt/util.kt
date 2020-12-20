package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.branch.Sequence
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.task.TaskTarget


fun seq(init: Sequence<BTParams>.() -> Unit): Sequence<BTParams> {
    val result = Sequence<BTParams>()
    result.init()
    return result
}

fun Sequence<BTParams>.task(name: String, exec: TaskExec): BaseTask {
//    val resultHolder = TaskResultTargetHolder()
    val task = BaseTask(name, exec)
    this.addChild(task)
    return task
}

fun tree(root: Task<BTParams>): BehaviorTree<BTParams> {
    return BehaviorTree(root)
}

fun result(status: Task.Status, nextTarget: Any? = null): TaskExecResult {
    return TaskExecResult(status, nextTarget?.let { TaskTarget(nextTarget) })
}

data class TaskResultTargetHolder(var target: TaskTarget? = null)