package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2

enum class Direction {
    H {
        override fun plus(point: GridPoint2, value: Int) = point.add(value, 0)
        override fun plus(point: Vector2, value: Float) = point.add(value, 0.0f)
    },
    V {
        override fun plus(point: GridPoint2, value: Int) = point.add(0, value)
        override fun plus(point: Vector2, value: Float) = point.add(0.0f, value)
    };

    abstract fun plus(point: GridPoint2, value: Int): GridPoint2
    abstract fun plus(point: Vector2, value: Float): Vector2
}