package com.ovle.rll3.model.module._deprecated.use

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.component.ComponentMappers.stash
import ktx.ashley.get

fun processStash(source: Entity, entity: Entity) {
    val stashComponent = entity[stash]
    with(stashComponent!!) {
        closed = !closed
    }
    val closed = stashComponent.closed
    entity[ComponentMappers.render]?.switchSprite(if (closed) "default" else "opened")

    send(Event.GameEvent.EntityEvent.EntityContentInteraction(source, entity))
}