package com.ovle.rll3.model.module.container

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event.GameEvent.EntityMovedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.system.EventSystem
import ktx.ashley.get
import ktx.ashley.has


//class CarrierSystem : EventSystem() {
//
//    override fun subscribe() {
//        EventBus.subscribe<EntityMovedEvent> { onEntityMovedEvent(it.entity) }
//    }
//
//    private fun onEntityMovedEvent(entity: Entity) {
//        if (!entity.has(carrier)) return
//
//        val carrierPosition = entity[position]!!.gridPosition
//        val carriedEntity = entity[carrier]!!.item ?: return
//
//        carriedEntity[position]!!.gridPosition = carrierPosition.cpy()
//    }
//}