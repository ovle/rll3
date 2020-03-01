package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2

class SightComponent(val radius: Int) : Component {
    var positions: Set<GridPoint2> = setOf()
}