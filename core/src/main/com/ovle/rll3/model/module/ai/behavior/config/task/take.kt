package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.task.checkValid
import ktx.ashley.get

//todo drop item on task cancellation
fun takeTask(targetHolder: TaskTargetHolder): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target()
    val carried = target.asEntity()

    val carrierComponent = owner[carrier]!!
    if (carrierComponent.item == carried) SUCCEEDED

    //todo fail if already taken by someone
    carrierComponent.item = carried
    //todo disable carried entity's systems?
    EventBus.send(Event.GameEvent.EntityCarryItemEvent(owner, carried))

    result(SUCCEEDED)
}