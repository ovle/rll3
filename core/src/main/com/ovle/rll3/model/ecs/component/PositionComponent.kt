package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.point

class PositionComponent(var position: Vector2 = Vector2()) : Component {
    val gridPosition: GridPoint2
        get() = point(position.x, position.y)
}