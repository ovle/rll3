package com.ovle.rll3

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


fun toGamePoint(screenPoint: Vector2, renderConfig: RenderConfig): Vector2 {
//    println("screenPoint $screenPoint")
    val mapScreenPoint = toMapScreenPoint(screenPoint, renderConfig)

    val x = mapScreenPoint.x / tileWidth
    val y = mapScreenPoint.y / tileHeight

    return Vector2(x, y)
}

fun toMapScreenPoint(screenPoint: Vector2, renderConfig: RenderConfig): Vector3 =
    renderConfig.unproject?.invoke(Vector3(screenPoint, 0.0f))!!


fun isNearHV(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return x1 == x2 && y1 in ((y2 - 1)..(y2 + 1))
        || y1 == y2 && x1 in ((x2 - 1)..(x2 + 1))
}

fun isNear(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return (x1 in ((x2 - 1)..(x2 + 1))) && (y1 in ((y2 - 1)..(y2 + 1)))
}