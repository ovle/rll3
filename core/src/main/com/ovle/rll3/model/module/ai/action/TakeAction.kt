//package com.ovle.rll3.model.module.ai.action
//
//import com.badlogic.ashley.core.Entity
//import com.ovle.rll3.event.Event
//import com.ovle.rll3.event.EventBus
//import com.ovle.rll3.model.module.ai.BaseTask
//import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
//import com.ovle.rll3.model.module.core.component.ComponentMappers.position
//import ktx.ashley.get
//
//class TakeAction: BaseTask() {
//
//    override fun executeIntr(): Status {
//        checkNotNull(owner[position])
//        checkNotNull(owner[carrier])
//
//        val targetEntity = target.unbox()
//        check(targetEntity is Entity)
//
//        val carrierComponent = owner[carrier]!!
//
//        if (carrierComponent.item == targetEntity) return Status.SUCCEEDED
//
//        carrierComponent.item = targetEntity
//        //todo disable carried entity's systems?
//
//        EventBus.send(Event.GameEvent.EntityCarryItemEvent(owner, targetEntity))
//
//        return Status.SUCCEEDED
//    }
//}