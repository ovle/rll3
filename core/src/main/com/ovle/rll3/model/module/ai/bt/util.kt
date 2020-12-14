package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.branch.Sequence
import com.ovle.rll3.TaskExec


fun action(exec: TaskExec): BaseTask = BaseTask(exec)

fun seq(vararg tasks: BaseTask): Sequence<BTParams> {
    return Sequence(*tasks)
}

fun tree(root: Task<BTParams>): BehaviorTree<BTParams> {
    return BehaviorTree(root)
}
