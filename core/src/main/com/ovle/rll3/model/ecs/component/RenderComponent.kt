package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.view.sprite.Sprite

class RenderComponent(
    var sprite: Sprite? = null,
    var visible: Boolean = true,
    val zLevel: Int = 0
) : Component