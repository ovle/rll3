package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.EntityDropItemEvent
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.container.Components.carrier
import com.ovle.rll3.model.module.space.position
import com.ovle.rll3.model.module.space.setPosition
import ktx.ashley.get

fun dropTask(): TaskExec = { (btParams) ->
    val owner = btParams.owner

    val to = owner.position()
    val carried = owner[carrier]!!.item!!
    carried.setPosition(to)
    owner[carrier]!!.item = null

    send(EntityDropItemEvent(owner, carried, to))

    result(SUCCEEDED)
}