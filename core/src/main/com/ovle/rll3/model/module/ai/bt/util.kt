package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.branch.Sequence
import com.ovle.rll3.TaskExec


//fun seq(vararg tasks: BaseTask): Sequence<BTParams> {
//    return Sequence(*tasks)
//}

fun seq(init: Sequence<BTParams>.() -> Sequence<BTParams>): Sequence<BTParams> {
    val result = Sequence<BTParams>()
    result.init()
    return result
}

fun Sequence<BTParams>.task(exec: TaskExec): BaseTask {
    val task = BaseTask(exec)
    this.addChild(task)
    return task
}

fun tree(root: Task<BTParams>): BehaviorTree<BTParams> {
    return BehaviorTree(root)
}

fun result(status: Task.Status, nextTarget: Any? = null): TaskExecResult {
    return TaskExecResult(status, nextTarget)
}
