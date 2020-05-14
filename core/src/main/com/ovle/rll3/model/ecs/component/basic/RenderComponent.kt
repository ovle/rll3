package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.template.AnimationType
import com.ovle.rll3.view.sprite.Sprite
import com.ovle.rll3.view.sprite.animation.FrameAnimation

class RenderComponent(
    var sprite: Sprite? = null,
    var visible: Boolean = true,
    var zLevel: Int = 0,
    var sprites: Map<String, Sprite> = mapOf(),
    var animations: Map<AnimationType, FrameAnimation> = mapOf()
) : Component {

    var currentAnimation: FrameAnimation? = null

    var flipped = false

    fun switchSprite(key: String) {
        sprite = sprites[key]
    }

    fun currentRegion(deltaTime: Float) = currentAnimation?.currentFrame(deltaTime) ?: sprite?.textureRegion()

    fun flip() {
        flipped = !flipped
    }
}