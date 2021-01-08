package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.*
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event.GameEvent.EntityStartMoveCommand
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.task.EntityConditions.isAtPosition
import com.ovle.rll3.model.module.task.EntityConditions.isMoving
import com.ovle.rll3.model.module.task.TaskTarget

//todo cancel movement on task cancellation
fun moveTask(targetHolder: TaskTargetHolder): TaskExec =  { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target
    target as TaskTarget
    val to = target.position()

    when {
        isAtPosition(owner, to) -> result(SUCCEEDED)
        isMoving(owner) -> result(RUNNING)
        else -> {
            send(EntityStartMoveCommand(owner, to))
            //fail will cancel all the tree
            //val status = if (EntityConditions.isMoving(owner)) RUNNING else FAILED
            result(RUNNING)
        }
    }
}