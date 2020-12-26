package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.bt.TaskTargetHolder
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.task.EntityConditions
import com.ovle.rll3.model.module.task.TaskTarget

fun moveTask(targetHolder: TaskTargetHolder): TaskExec =  { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target
    target as TaskTarget
    val to = target.position()

    when {
        EntityConditions.isAtPosition(owner, to) -> result(Task.Status.SUCCEEDED)
        EntityConditions.isMoving(owner) -> result(Task.Status.RUNNING)
        else -> {
            EventBus.send(Event.GameEvent.EntityStartMoveCommand(owner, to))
            val status = if (EntityConditions.isMoving(owner)) Task.Status.RUNNING else Task.Status.FAILED
            result(status)
        }
    }
}