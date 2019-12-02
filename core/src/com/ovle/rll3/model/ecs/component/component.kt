package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable

class PlayerControlledComponent : Component

class PositionComponent(var position: Vector2) : Component

class LightComponent(val radius: Int) : Component

class MoveComponent(val tilesPerSecond: Float = 2f) : Component {

    private val path: MutableList<Vector2> = mutableListOf()

    private var currentIndex = -1
    private fun currentIndexValid() = currentIndex in (0 until path.size)

//    fun currentFrom() = if (currentIndexValid()) path[currentIndex] else null
    fun currentTo() = if (currentIndexValid()) path[currentIndex] else null
    fun finished() = currentIndex >= path.size
    fun started() = currentIndex >= 0

    fun start(){
        check(!finished())
        currentIndex = 0
    }

    fun next() {
        check(!finished())
        currentIndex++
    }

    fun add(newTo: Vector2) {
        path.add(newTo)
    }

    fun reset() {
        path.clear()
        currentIndex = -1
    }
}

class SizeComponent(var size: Vector2) : Component

class RenderComponent(
        var sprite: SpriteDrawable,
        var visible: Boolean = true,
        val zLevel: Int = 0
) : Component

val NO_ANIMATION = Animation<TextureRegion>(0f)

class AnimationComponent(var animation: Animation<TextureRegion> = NO_ANIMATION) : Component