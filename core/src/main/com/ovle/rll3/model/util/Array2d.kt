package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2

class Array2d<T>(
    val data: Array<T>,
    val size: Int
) {
    fun index(x: Int, y: Int) = size * y + x
    fun point(index: Int) = GridPoint2(
        index % size,
        size - 1 - index / size
    )

    operator fun get(x: Int, y: Int): T = data[index(x, y)]
    operator fun set(x: Int, y: Int, el: T) { data[index(x, y)] = el }

    fun indexedElements() = data.mapIndexed { index, el -> index to el }
    fun points() = data.mapIndexed { index, _ -> point(index) }

    fun isPointValid(point: GridPoint2) = isPointValid(point.x, point.y)
    fun isPointValid(x: Int, y: Int) = x in (0 until size) && y in (0 until size)
}