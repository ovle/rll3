package com.ovle.rll3.model.lightning

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.point

fun lightSource(x: Int, y: Int, radius: Int): Entity {
    return Entity().apply {
        add(PositionComponent(point(x, y)))
        add(LightComponent(
            radius = radius,
            lightPositions = listOf()
        ))
    }
}