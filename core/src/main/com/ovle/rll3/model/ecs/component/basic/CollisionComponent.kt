package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.ashley.core.Component

class CollisionComponent(
    var active: Boolean = true,
    var passable4Body: Boolean = false,
    var passable4Light: Boolean = false
) : Component