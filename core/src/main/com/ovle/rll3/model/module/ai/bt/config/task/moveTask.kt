package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task.Status.*
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.bt.TaskTargetHolder
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.task.EntityConditions
import com.ovle.rll3.model.module.task.TaskTarget

//todo cancel movement on task cancellation
fun moveTask(targetHolder: TaskTargetHolder): TaskExec =  { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target
    target as TaskTarget
    val to = target.position()

    when {
        EntityConditions.isAtPosition(owner, to) -> result(SUCCEEDED)
        EntityConditions.isMoving(owner) -> result(RUNNING)
        else -> {
            EventBus.send(Event.GameEvent.EntityStartMoveCommand(owner, to))
            //fail will cancel all the tree
            //val status = if (EntityConditions.isMoving(owner)) RUNNING else FAILED
            result(RUNNING)
        }
    }
}