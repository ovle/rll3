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

fun Sequence<BTParams>.task(name: String, exec: TaskExec): TaskTargetHolder {
    val resultHolder = TaskTargetHolder()
    val task = BaseTask(name, exec, resultHolder)
    this.addChild(task)
    return resultHolder
}

fun tree(init: BehaviorTree<BTParams>.() -> Task<BTParams>): BehaviorTree<BTParams> {
    val result = BehaviorTree<BTParams>()
    val root = result.init()
    result.addChild(root)
    return result
}

fun result(status: Task.Status, nextTarget: Any? = null): TaskExecResult {
    return TaskExecResult(status, nextTarget?.let { TaskTarget(nextTarget) })
}

data class TaskTargetHolder(var target: TaskTarget? = null)

