package com.ovle.rll3.model.module.ai

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.branch.Sequence
import com.ovle.rll3.TaskExec


//fun task(exec: TaskExec): BaseTask = object : BaseTask() {
//    override fun executeIntr(): Status = exec.invoke(this.`object`)
//}

fun task(exec: TaskExec): BaseTask = BaseTask(exec)

fun seq(vararg tasks: BaseTask): Sequence<BaseBlackboard> {
    return Sequence(*tasks)
}


val config = seq(
    task { _ -> println("1"); Task.Status.FAILED },
    task { _ -> println("2"); Task.Status.SUCCEEDED }
)