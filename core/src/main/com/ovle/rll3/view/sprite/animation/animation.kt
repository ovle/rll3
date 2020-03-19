package com.ovle.rll3.view.sprite.animation

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.view.layer.TextureRegions

data class AnimationTemplate(
    val id: String,
    val frames: Array<Pair<Int, Int>>,
    val frameDuration: Float = 0.25f,
    val repeat: Boolean = false,
    val alwaysPlaying: Boolean = false,  //should be play by default if no other animation is playing
    val isTerminal: Boolean = false  //no default after it finished
)

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

    fun flip() {
        frames.forEach { it?.flip(true, false) }
    }
}