package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2

class MoveComponent(val tilesPerSecond: Float = 2f, val path: MovePath = MovePath()) : Component
class MovePath {
    private val path: MutableList<GridPoint2> = mutableListOf()
    private var currentIndex = -1

    private val currentIndexValid: Boolean
        get() = currentIndex in (0 until path.size)

    val currentTarget: GridPoint2?
        get() = if (currentIndexValid) path[currentIndex] else null

    val finished: Boolean
        get() = currentIndex >= path.size

    val started: Boolean
        get() = currentIndex >= 0


    fun start(){
        check(!finished)
        currentIndex = 0
    }

    fun next() {
        check(!finished)
        currentIndex++
    }

    fun add(newTarget: GridPoint2) {
        path.add(newTarget)
    }

    fun set(newPath: List<GridPoint2>) {
        reset()
        path.addAll(newPath)
    }

    fun reset() {
        path.clear()
        currentIndex = -1
    }

    override fun toString(): String {
        return """
            path: [${path.map { it }.joinToString(", ")}]
        """.trimIndent()
    }
}