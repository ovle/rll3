package com.ovle.rll3.model.module.render

import com.ovle.rll3.model.module.core.component.BaseComponent
import com.ovle.rll3.view.sprite.Sprite

class RenderComponent(
    var sprite: Sprite? = null,
    var visible: Boolean = true,
    var zLevel: Int = 0,
    var sprites: Map<String, Sprite> = mapOf()
) : BaseComponent {

    var currentAnimation: Animation? = null

    var flipped = false

    fun switchSprite(key: String) {
        sprite = sprites[key]
    }

    fun currentRegion() = sprite?.textureRegion()

    fun flip() {
        flipped = !flipped
    }
}

