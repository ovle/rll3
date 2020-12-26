package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.entity.setPosition
import ktx.ashley.get

fun dropTask(): TaskExec = { (btParams) ->
    val owner = btParams.owner

    val to = owner.position()
    val carried = owner[carrier]!!.item!!
    carried.setPosition(to)
    owner[carrier]!!.item = null

    EventBus.send(Event.GameEvent.EntityDropItemEvent(owner, carried, to))

    result(Task.Status.SUCCEEDED)
}