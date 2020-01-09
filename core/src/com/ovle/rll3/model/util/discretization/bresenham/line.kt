package com.ovle.rll3.model.util.discretization.bresenham

import com.ovle.rll3.model.ecs.component.TilePosition
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


fun line(fromPosition: TilePosition, toPosition: TilePosition): List<TilePosition> {
    val (fromX, fromY) = fromPosition
    val (toX, toY) = toPosition
    val stepsX = toX - fromX
    val stepsY = toY - fromY
    val mainIsX = abs(stepsX) > abs(stepsY)
    val minX = min(fromX, toX)
    val maxX = max(fromX, toX)
    val minY = min(fromY, toY)
    val maxY = max(fromY, toY)
    val fromXIsMin = minX == fromX
    val fromYIsMin = minY == fromY
    val stepsYtoX = stepsY.toFloat() / stepsX
    val stepsXtoY = stepsX.toFloat() / stepsY

    return when {
        mainIsX -> when {
            fromXIsMin -> (minX..maxX)
            else -> (maxX downTo minX)
        }.mapIndexed { index, it ->
            it to fromY + (index * stepsYtoX).toInt()
        }
        else -> when {
            fromYIsMin -> (minY..maxY)
            else -> (maxY downTo minY)
        }.mapIndexed { index, it ->
            fromX + (index * stepsXtoY).toInt() to it
        }
    }
}