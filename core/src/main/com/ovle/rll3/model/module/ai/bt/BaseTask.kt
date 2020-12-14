package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.TaskExec


class BaseTask(var exec: TaskExec? = null): LeafTask<BTParams>() {

    override fun copyTo(otherTask: Task<BTParams>): Task<BTParams> {
        otherTask as BaseTask
        otherTask.exec = exec
        return otherTask
    }

    override fun execute(): Status {
        val target = null   //todo how to pass target?
        val params = TaskExecParams(this.`object`, target)
        return this.exec!!.invoke(params)
    }
}

