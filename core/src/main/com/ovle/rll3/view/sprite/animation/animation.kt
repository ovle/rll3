package com.ovle.rll3.view.sprite.animation

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.template.AnimationTemplate
import com.ovle.rll3.view.layer.TextureRegions

class FrameAnimation(
    regions: TextureRegions,
    val template: AnimationTemplate
) {
    private val frames: Array<TextureRegion?> = framesFromRegions(regions, template.frames)
    private var animation: Animation<TextureRegion?> = Animation(template.frameDuration, *frames)
    private var stateTime: Float = 0.0f

    private fun framesFromRegions(regions: Array<Array<TextureRegion>>, frames: Array<Pair<Int, Int>>): Array<TextureRegion?> {
        return frames.map {
            (x, y) -> regions[y][x] //switched
        }.toTypedArray()
    }

    fun currentFrame(deltaTime: Float): TextureRegion? {
        stateTime += deltaTime
        return animation.getKeyFrame(stateTime, template.repeat);
    }

    fun reset() {
        stateTime = 0.0f
    }

    fun isFinished() = animation.isAnimationFinished(stateTime)
}