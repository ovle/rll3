package com.ovle.rll3.model.module._deprecated.use

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.collision
import com.ovle.rll3.model.module.core.component.ComponentMappers.door
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.render
import ktx.ashley.get

fun processDoor(source: Entity, entity: Entity) {
    val doorComponent = entity[door]
    with(doorComponent!!) {
        closed = !closed
    }
    val closed = doorComponent.closed
    entity[collision]?.active = closed
    entity[render]?.switchSprite(if (closed) "default" else "opened")

    send(
        Event.GameEvent.UpdateLightCollisionCommand(arrayOf(entity[position]!!.gridPosition))
    )
}