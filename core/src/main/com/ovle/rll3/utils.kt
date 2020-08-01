package com.ovle.rll3

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import kotlin.math.roundToInt
import kotlin.random.Random


fun IntRange.random(r: Random) = (r.nextDouble() * (this.last - this.first)).roundToInt() + this.first
fun Int.withChance(chance: Float, r: Random): Int = if (r.nextDouble() <= chance) this else 0
fun <T> T.withChance(chance: Float, r: Random, defaultValue: T) = if (r.nextFloat() <= chance) this else defaultValue

operator fun Vector2.component1() = x
operator fun Vector2.component2() = y

operator fun GridPoint2.component1() = x
operator fun GridPoint2.component2() = y

fun point(x: Int = 0, y: Int = 0) = GridPoint2(x, y)
fun point(x: Float = 0.0f, y: Float = 0.0f) = GridPoint2(x.roundToClosestByAbsInt(), y.roundToClosestByAbsInt())
fun point(floatPoint: Vector2) = point(floatPoint.x, floatPoint.y)
fun point(point: GridPoint2) = point(point.x, point.y)

fun vec2(point: GridPoint2) = Vector2(point.x.toFloat(), point.y.toFloat())

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

fun Float.roundToClosestByAbsInt() = if (this > 0) this.roundToInt() else -((-this).roundToInt())

fun GridPoint2.near() = (x-1..x+1).map { tx -> (y-1..y+1).map { ty -> point(tx, ty) } }.flatten()
fun GridPoint2.nearExclusive() = near().filter { it != this }