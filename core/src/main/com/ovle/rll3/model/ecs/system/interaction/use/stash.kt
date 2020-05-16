package com.ovle.rll3.model.ecs.system.interaction.use

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get

fun processContainer(source: Entity, stash: Entity) {
    val containerComponent = stash[Mappers.container]
    with(containerComponent!!) {
        closed = !closed
    }
    val closed = containerComponent.closed
    stash[Mappers.render]?.switchSprite(if (closed) "default" else "opened")

    //todo inventory
}