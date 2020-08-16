package com.ovle.rll3.model.module.render

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.util.Direction


sealed class TransformAnimation(val totalLength: Float) {
    class ShiftAnimation(
        val direction: Direction,
        val frames: Array<Int>,
        val frameLength: Float
    ): TransformAnimation(frameLength * frames.size) {

        override fun process(point: Vector2, animation: Animation): Vector2 {
            val frameIndex = (animation.time / frameLength).toInt()
            val frameValue = frames[frameIndex]
            return direction.plus(point.cpy(), frameValue.toFloat())
        }
    }

    open fun process(point: Vector2, animation: Animation): Vector2 {
        return point
    }
    open fun process(region: TextureRegion, animation: Animation): TextureRegion {
        return region
    }
}

data class Animation(
    val type: AnimationType,
    var time: Float = 0.0f
)

enum class AnimationType {
    Walk,
    Act,
    ActSubject,
    Death;
}