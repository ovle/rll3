package com.ovle.rll3.model.module.ai.action

import com.ovle.rll3.event.Event.GameEvent.EntityDropItemEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.BaseTask
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import ktx.ashley.get

//class DropAction: BaseTask() {
//
//    override fun executeIntr(): Status {
//        checkNotNull(owner[position])
//        checkNotNull(owner[carrier])
//
//        val targetPosition = owner[position]!!.gridPosition
//
//        val droppedEntity = owner[carrier]!!.item
//        checkNotNull(droppedEntity)
//
//        droppedEntity[position]!!.gridPosition = targetPosition
//        //todo enable carried entity's systems?
//
//        owner[carrier]!!.item = null
//
//        EventBus.send(EntityDropItemEvent(owner, droppedEntity, targetPosition))
//
//        return Status.SUCCEEDED
//    }
//}