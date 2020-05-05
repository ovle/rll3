package com.ovle.rll3

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.ovle.rll3.model.util.config.RenderConfig
import com.ovle.rll3.view.tileHeight
import com.ovle.rll3.view.tileWidth
import kotlin.math.roundToInt


fun IntRange.random() = (Math.random() * (this.last - this.first)).roundToInt() + this.first
fun <T> List<T>.random(): T? = if (this.isEmpty()) null else this[(0 until size).random()]
fun Int.withChance(chance: Float): Int = if (Math.random() <= chance) this else 0

operator fun Vector2.component1() = x
operator fun Vector2.component2() = y

operator fun GridPoint2.component1() = x
operator fun GridPoint2.component2() = y

fun point(x: Int = 0, y: Int = 0) = GridPoint2(x, y)
fun point(x: Float = 0.0f, y: Float = 0.0f) = GridPoint2(x.roundToClosestByAbsInt(), y.roundToClosestByAbsInt())
fun point(floatPoint: Vector2) = point(floatPoint.x, floatPoint.y)
fun point(point: GridPoint2) = point(point.x, point.y)

fun floatPoint(x: Float, y: Float) = Vector2(x, y)
fun floatPoint(point: GridPoint2) = Vector2(point.x.toFloat(), point.y.toFloat())


fun toGamePoint(screenPoint: Vector2, renderConfig: RenderConfig): Vector2 {
    val mapScreenPoint = toMapScreenPoint(screenPoint, renderConfig)

    val x = mapScreenPoint.x / tileWidth
    val y = mapScreenPoint.y / tileHeight

    return Vector2(x, y)
}

fun toMapScreenPoint(screenPoint: Vector2, renderConfig: RenderConfig): Vector3 =
    renderConfig.unproject?.invoke(Vector3(screenPoint, 0.0f))!!


fun isNearHV(p1: GridPoint2, p2: GridPoint2?) =
    if (p2 == null) false
    else isNearHV(p1.x, p1.y, p2.x, p2.y)

fun isNearHV(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return x1 == x2 && y1 in ((y2 - 1)..(y2 + 1))
        || y1 == y2 && x1 in ((x2 - 1)..(x2 + 1))
}

fun isNear(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return (x1 in ((x2 - 1)..(x2 + 1))) && (y1 in ((y2 - 1)..(y2 + 1)))
}

public fun Float.roundToClosestByAbsInt() = if (this > 0) this.roundToInt() else -((-this).roundToInt())