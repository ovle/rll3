package com.ovle.rll3.model.module.collision

import com.ovle.rll3.model.module.core.component.BaseComponent

class CollisionComponent(
    var active: Boolean = true,
    var passable4Body: Boolean,
    var passable4Light: Boolean
) : BaseComponent