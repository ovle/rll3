package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.TaskExec


class BaseTask(var name: String? = "", var exec: TaskExec? = null): LeafTask<BTParams>() {

    override fun copyTo(otherTask: Task<BTParams>): Task<BTParams> {
        otherTask as BaseTask
        otherTask.name = name
        otherTask.exec = exec
        return otherTask
    }

    override fun execute(): Status {
        val btParams = this.`object`
        val initialTarget = btParams.btTaskTarget
        val target = btParams.currentTarget ?: initialTarget   //todo
        val params = TaskExecParams(btParams, target)
        val execResult = this.exec!!.invoke(params)

        btParams.currentTarget = execResult.nextTarget

        return execResult.status
    }
}

