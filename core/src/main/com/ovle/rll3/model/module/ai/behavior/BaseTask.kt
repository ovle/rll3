package com.ovle.rll3.model.module.ai.behavior

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.Task.Status.*
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.task.InvalidTargetException


class BaseTask(var name: String? = "", var exec: TaskExec? = null, var holder: TaskTargetHolder? = null): LeafTask<BTParams>() {

    override fun copyTo(otherTask: Task<BTParams>): Task<BTParams> {
        otherTask as BaseTask
        otherTask.name = name
        otherTask.exec = exec
        otherTask.holder = holder
        return otherTask
    }

    override fun execute(): Status {
        val btParams = this.`object`
        val params = TaskExecParams(btParams)
        val execResult: TaskExecResult
        try {
            execResult = this.exec!!.invoke(params)
        } catch (ex: InvalidTargetException) {
            println(" > > > ${name}: target is no longer valid")
            return FAILED
        }

        val status = execResult.status

//        println(" > > > ${name}... $status")
        if (status == SUCCEEDED) {
            holder?.target = execResult.nextTarget
//            println(" > > > new target: ${execResult.nextTarget?.target.info()}")
        }

        return status
    }
}

