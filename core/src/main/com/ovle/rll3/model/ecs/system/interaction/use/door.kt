package com.ovle.rll3.model.ecs.system.interaction.use

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.util.Mappers
import ktx.ashley.get

fun processDoor(source: Entity, door: Entity) {
    val doorComponent = door[Mappers.door]
    with(doorComponent!!) {
        closed = !closed
    }
    val closed = doorComponent.closed
    door[Mappers.collision]?.active = closed
    door[Mappers.render]?.visible = closed    //todo change sprite

    //todo update fov for all entities with this door in fov
}