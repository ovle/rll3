package com.ovle.rll3.model.ecs.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

val NO_ANIMATION = Animation<TextureRegion>(0f)

typealias TilePosition = Pair<Int, Int>


class MovePath {
    private val path: MutableList<Vector2> = mutableListOf()

    private var currentIndex = -1
    private fun currentIndexValid() = currentIndex in (0 until path.size)

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