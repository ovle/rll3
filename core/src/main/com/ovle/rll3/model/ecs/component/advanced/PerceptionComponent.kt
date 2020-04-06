package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2

//todo sight, hearing, feel living, feel magic
class PerceptionComponent(
    val sightRadius: Int?,
    val hearRadius: Int?
) : Component {
    var sightPositions: Set<GridPoint2> = setOf()
}