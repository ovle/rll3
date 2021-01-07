package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event.GameEvent.EntityEatEvent
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.ai.bt.TaskTargetHolder
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.task.TaskTarget

fun eatTask(targetHolder: TaskTargetHolder): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target
    target as TaskTarget

    val food = target.asEntity()
    send(EntityEatEvent(owner, food))

    result(SUCCEEDED)
}