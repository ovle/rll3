package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.view.initialScale
import com.ovle.rll3.view.screenHeight
import com.ovle.rll3.view.screenWidth

class PlayerControlledComponent : Component

class PositionComponent(var position: Vector2) : Component

class LightComponent(val radius: Int) : Component

class DoorComponent() : Component

class SightComponent(val radius: Int) : Component {
    var positions: Set<TilePosition> = setOf()
}

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

    fun set(newPath: List<Vector2>) {
        reset()
        path.addAll(newPath)
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

//todo make entity/component?
data class RenderConfig(
    var scale: Float = initialScale,
    var scrollOffset: Vector2 = Vector2(screenWidth / 2, screenHeight / 2),
    var unproject: ((Vector3) -> Vector3)? = null
)

typealias TilePosition = Pair<Int, Int>

val renderConfig = RenderConfig()

lateinit var tileMap: TileArray
