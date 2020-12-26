package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.branch.Selector
import com.badlogic.gdx.ai.btree.branch.Sequence
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.task.TaskTarget


fun Task<BTParams>.task(name: String, exec: TaskExec): TaskTargetHolder {
    val resultHolder = TaskTargetHolder()
    val task = BaseTask(name, exec, resultHolder)
    this.addChild(task)
    return resultHolder
}

fun Task<BTParams>.seq(init: Sequence<BTParams>.() -> Unit): Sequence<BTParams> {
    val result = Sequence<BTParams>()
    result.init()
    this.addChild(result)
    return result
}

fun Task<BTParams>.select(init: Selector<BTParams>.() -> Unit): Selector<BTParams> {
    val result = Selector<BTParams>()
    result.init()
    this.addChild(result)
    return result
}

fun tree(init: BehaviorTree<BTParams>.() -> Unit): BehaviorTree<BTParams> {
    val result = BehaviorTree<BTParams>()
    result.init()
    return result
}

fun result(status: Task.Status, nextTarget: Any? = null): TaskExecResult {
    return TaskExecResult(status, nextTarget?.let { TaskTarget(nextTarget) })
}

data class TaskTargetHolder(var target: TaskTarget? = null)

