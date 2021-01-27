package com.ovle.rll3

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.component.ComponentMappers.template
import com.ovle.rll3.model.module.core.entity.name
import com.ovle.rll3.model.module.health.HealthComponent
import com.ovle.rll3.model.module.space.PositionComponent
import ktx.ashley.get
import ktx.ashley.has
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

fun rectangle(x1: Int, y1: Int, x2: Int, y2: Int) = Rectangle(
    x1.toFloat(), y1.toFloat(), (x2 - x1).toFloat(), (y2 - y1).toFloat()
)
fun Rectangle.rangeX() = (x.toInt()..x.toInt() + width.toInt())
fun Rectangle.rangeY() = (y.toInt()..y.toInt() + height.toInt())
fun Rectangle.points() = cartesianProduct(rangeX().toList(), rangeY().toList())
    .map { (x, y) -> point(x, y) }

//--- todo builder

fun GridPoint2.adjacentHV() = adjacentHV(x, y)
fun GridPoint2.adjacentD() = adjacentD(x, y)

fun adjacentHV(x: Int, y: Int) = arrayOf(
    point(x - 1, y),
    point(x + 1, y),
    point(x, y - 1),
    point(x, y + 1)
)

fun adjacentD(x: Int, y: Int) = arrayOf(
    point(x - 1, y - 1),
    point(x + 1, y + 1),
    point(x + 1, y - 1),
    point(x - 1, y + 1)
)

fun isAdjacentHV(p1: GridPoint2, p2: GridPoint2?) =
    if (p2 == null) false
    else isAdjacentHV(p1.x, p1.y, p2.x, p2.y)

fun isAdjacentHV(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return x1 == x2 && y1 in ((y2 - 1)..(y2 + 1))
        || y1 == y2 && x1 in ((x2 - 1)..(x2 + 1))
}

fun isAdjacent(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    return (x1 in ((x2 - 1)..(x2 + 1))) && (y1 in ((y2 - 1)..(y2 + 1)))
}

fun GridPoint2.adjacent() = (x-1..x+1).map { tx -> (y-1..y+1).map { ty -> point(tx, ty) } }.flatten()
fun GridPoint2.adjacentExclusive() = adjacent().filter { it != this }
fun GridPoint2.isAdjacent(other: GridPoint2, delta: Int = 1) = this.dst(other) <= delta

fun Grid.isPointValid(x: Int, y: Int) = x in (0 until width) && y in (0 until height)
fun TileArray.toGrid() = Grid(data.map { it.toFloat() }.toFloatArray(), size, size)

//---

fun Float.roundToClosestByAbsInt() = if (this > 0) this.roundToInt() else -((-this).roundToInt())

fun <T, U> cartesianProduct(c1: Collection<T>, c2: Collection<U>): List<Pair<T, U>> {
    return c1.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}
fun <T, U> cartesianProduct(c1: Array<T>, c2: Array<U>): Array<Pair<T, U>> {
    return c1.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }.toTypedArray()
}

fun <E: Enum<E>> E.next(): E {
    val enumConstants = this.declaringClass.enumConstants
    val nextOrdinal = if (this.ordinal == enumConstants.size - 1) 0 else this.ordinal + 1
    return enumConstants[nextOrdinal]
}
fun <E: Enum<E>> E.prev(): E {
    val enumConstants = this.declaringClass.enumConstants
    val nextOrdinal = if (this.ordinal == 0) enumConstants.size - 1 else this.ordinal - 1
    return enumConstants[nextOrdinal]
}

fun Collection<Entity>.info(): String {
    val entityInfos = this.map { it.info() }
    return entityInfos.groupBy { it }.entries.joinToString(", ") { (k, v) -> "${v.count()}x $k" }
}

fun Any?.info(recursive: Boolean = false): String = when {
    this == null -> "(nothing)"
    this is Entity -> when {
            this.has(template) -> this.name()
            else -> "(unknown entity)"
        } + if (recursive) {
            this.components.map { it.info() }
            .filterNot { it.isBlank() }
            .joinToString(
                prefix = " {\n",
                postfix = "\n}",
                separator = "\n",
                transform = { s -> "  $s" }
            )
        } else ""
    this is Component -> when {
        this is HealthComponent -> "he: [he:${health}/st:${stamina}/hu:${hunger}]"
        this is PositionComponent -> "pos: [${gridPosition.info()}]"
        else -> ""
    }
    this is GridPoint2 -> this.toString()
    else -> "(unknown)"
}