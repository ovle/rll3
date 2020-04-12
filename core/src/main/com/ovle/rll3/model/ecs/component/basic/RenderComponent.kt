package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.ashley.core.Component
import com.ovle.rll3.view.sprite.Sprite

class RenderComponent(
    var sprite: Sprite? = null,
    var portrait: Sprite? = null,
    var visible: Boolean = true,
    var zLevel: Int = 0
) : Component