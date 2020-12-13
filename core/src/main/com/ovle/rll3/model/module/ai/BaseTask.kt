package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.task.TaskTarget.EntityTarget
import com.ovle.rll3.model.module.task.TaskTarget.PositionTarget
import ktx.ashley.get

class BaseTask(var exec: TaskExec? = null): LeafTask<BaseBlackboard>() {

    protected val owner: Entity
        get() = task.performer!!

    protected val task
        get() = this.`object`.task

    protected val engine
        get() = this.`object`.engine

    protected val target
        get() = this.`object`.task.target

    protected val entities
        get() = engine.entities.toList()

    var customTarget: Any? = null

    protected val targetPosition
        get() = when {
            customTarget != null -> customTarget as GridPoint2
            target is EntityTarget -> (target as EntityTarget).entity[position]!!.gridPosition
            target is PositionTarget -> (target as PositionTarget).position
            else -> throw UnsupportedOperationException()
        }

    override fun copyTo(otherTask: Task<BaseBlackboard>): Task<BaseBlackboard> {
        otherTask as BaseTask
        otherTask.exec = exec
        return otherTask
    }

    override fun execute(): Status {
//        println("execute ${this.javaClass}")
        return this.exec!!.invoke(this.`object`)
    }
}