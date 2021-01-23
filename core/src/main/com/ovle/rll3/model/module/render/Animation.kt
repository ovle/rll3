package com.ovle.rll3.model.module.render

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.ExactTurn
import com.ovle.rll3.model.util.Direction


sealed class Animation(val totalLength: ExactTurn) {

    class ShiftAnimation(
        val direction: Direction,
        val frames: Array<Int>,
        val frameLength: ExactTurn
    ): Animation(frameLength * frames.size) {

        override fun process(point: Vector2, animation: AnimationInfo): Vector2 {
            val frameIndex = (animation.time / frameLength).toInt()
            val frameValue = frames[frameIndex]
            return direction.plus(point.cpy(), frameValue.toFloat())
        }
    }

    class BlinkAnimation(
        val blinkRegion: TextureRegion?,
        val frameLength: ExactTurn
    ): Animation(frameLength * 2) {

        override fun process(region: TextureRegion, animation: AnimationInfo): TextureRegion? {
            val frameIndex = (animation.time / frameLength).toInt()
            val isOdd = frameIndex % 2 == 0
            return if (isOdd) region else blinkRegion
        }
    }

    open fun process(point: Vector2, animation: AnimationInfo): Vector2 {
        return point
    }
    open fun process(region: TextureRegion, animation: AnimationInfo): TextureRegion? {
        return region
    }
}

data class AnimationInfo(
    val animation: Animation,
    var time: ExactTurn = 0.0
)