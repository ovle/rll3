package com.ovle.rll3.model.module._deprecated.use

//import com.badlogic.ashley.core.Entity
//import com.ovle.rll3.event.Event.GameEvent.UpdateLightCollisionCommand
//import com.ovle.util.event.EventBus.send
//import com.ovle.rll3.model.module.core.component.ComponentMappers.collision
//import com.ovle.rll3.model.module.core.component.ComponentMappers.door
//import com.ovle.rll3.model.module.core.component.ComponentMappers.render
//import com.ovle.rll3.model.module.core.entity.position
//import ktx.ashley.get
//
//fun processDoor(source: Entity, entity: Entity) {
//    val doorComponent = entity[door]
//    with(doorComponent!!) {
//        closed = !closed
//    }
//    val closed = doorComponent.closed
//    entity[collision]?.active = closed
//    entity[render]?.switchSprite(if (closed) "default" else "opened")
//
//    send(UpdateLightCollisionCommand(arrayOf(entity.position())))
//}