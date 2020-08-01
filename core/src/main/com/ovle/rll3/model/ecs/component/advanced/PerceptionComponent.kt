package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.BaseComponent

//todo sight, hearing, feel living, feel magic
class PerceptionComponent(
    val sightRadius: Int?,
    val hearRadius: Int? = 0
) : BaseComponent {
    var fov: Set<GridPoint2> = setOf()
}