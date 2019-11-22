package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable

class PlayerComponent : Component

class PositionComponent(var position: Vector2) : Component

class MoveComponent(val tilesPerSecond: Float = 2f, initialPath: List<Vector2>?) : Component {

    private val path: MutableList<Vector2> = initialPath?.toMutableList() ?: mutableListOf()
    val from = path.first()
    val to = path.last()

    private var currentIndex = 0
    fun currentFrom() = if (currentIndex < path.size) path[currentIndex] else null
    fun currentTo() = if (currentIndex < path.size) path[currentIndex + 1] else null
    fun finished() = currentIndex >= path.size - 1

    fun next() {
        check(!finished())

        currentIndex++
    }

    fun add(newTo: Vector2) {
        path.add(newTo)
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