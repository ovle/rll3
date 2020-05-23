package com.ovle.rll3.model.ecs.system.interaction.use

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers.collision
import com.ovle.rll3.model.ecs.component.util.Mappers.door
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.component.util.Mappers.render
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
        Event.UpdateLightCollision(arrayOf(entity[position]!!.gridPosition))
    )
}