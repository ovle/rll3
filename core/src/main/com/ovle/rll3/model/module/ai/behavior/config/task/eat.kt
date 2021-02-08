package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.EntityEatEvent
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result

fun eatTask(targetHolder: TaskTargetHolder): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target()
    val food = target.asEntity()
    send(EntityEatEvent(owner, food))

    result(SUCCEEDED)
}