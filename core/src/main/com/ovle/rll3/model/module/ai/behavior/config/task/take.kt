package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rll3.TaskExec
import com.ovle.rll3.event.EntityCarryItemEvent
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.entity.carriers
import ktx.ashley.get

//todo drop item on task cancellation
fun takeTask(targetHolder: TaskTargetHolder): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val target = targetHolder.target()
    val carried = target.asEntity()

    val carrierComponent = owner[carrier]!!
    if (carrierComponent.item == carried) SUCCEEDED

    val carriers = btParams.location.entities.carriers()
    val isAlreadyTaken = carriers.any { it[carrier]!!.item == carried }
    if (isAlreadyTaken) FAILED

    carrierComponent.item = carried
    //todo disable carried entity's systems?
    send(EntityCarryItemEvent(owner, carried))

    result(SUCCEEDED)
}