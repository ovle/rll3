package com.ovle.rll3.model.module.render

import com.ovle.rlUtil.gdx.view.AnimationInfo
import com.ovle.rlUtil.gdx.view.Sprite
import com.ovle.rll3.model.module.core.component.EntityComponent

class RenderComponent(
    var sprite: Sprite? = null,
    var visible: Boolean = true,
    var zLevel: Int = 0,
    var sprites: Map<String, Sprite> = mapOf()
) : EntityComponent() {

    var currentAnimation: AnimationInfo? = null

    var flipped = false

    fun switchSprite(key: String) {
        sprite = sprites[key]
    }

    fun currentRegion() = sprite?.textureRegion()

    fun flip() {
        flipped = !flipped
    }
}