package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable

class PlayerComponent : Component

class PositionComponent(var position: Vector2) : Component

class MoveComponent(val tilesPerSecond: Float = 2f, val path: List<Vector2> = listOf()) : Component {
    init{
        require(path.size > 1)
    }

    val from = path.first()
    val to = path.last()

    private var currentIndex = 0
    fun currentFrom() = path[currentIndex]
    fun currentTo() = path[currentIndex + 1]
    fun finished() = currentIndex >= path.size - 1

    fun next() {
        println("next")
        check(!finished())

        currentIndex++
    }
}

class SizeComponent(var size: Vector2) : Component

class RenderComponent(
        var sprite: SpriteDrawable,
        var visible: Boolean = true,
        val zLevel: Int = 0
) : Component