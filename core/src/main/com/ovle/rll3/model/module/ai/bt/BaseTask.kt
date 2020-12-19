package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.task.TaskTarget


class BaseTask(var exec: TaskExec? = null): LeafTask<BTParams>() {

    override fun copyTo(otherTask: Task<BTParams>): Task<BTParams> {
        otherTask as BaseTask
        otherTask.exec = exec
        return otherTask
    }

    override fun execute(): Status {
        val btParams = this.`object`
        val initialTarget = btParams.target.unbox()
        val target = btParams.currentTarget ?: initialTarget   //todo
        val params = TaskExecParams(btParams, target)
        val execResult = this.exec!!.invoke(params)

        btParams.currentTarget = TaskTarget(execResult.nextTarget)

        return execResult.status
    }
}

