package com.ovle.rll3.model.util.discretization.bresenham

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.component1
import com.ovle.rll3.model.ecs.component.component2
import com.ovle.rll3.model.ecs.component.point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun line(fromPosition: GridPoint2, toPosition: GridPoint2): List<GridPoint2> {
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

    val stepsYtoX = stepsY.toFloat() / if (stepsX == 0) 1 else abs(stepsX)
    val stepsXtoY = stepsX.toFloat() / if (stepsY == 0) 1 else abs(stepsY)

    return when {
        mainIsX -> when {
            fromXIsMin -> (minX..maxX)
            else -> (maxX downTo minX)
        }.mapIndexed { index, x ->
           point(x, fromY + (index * stepsYtoX).roundToInt())
        }
        else -> when {
            fromYIsMin -> (minY..maxY)
            else -> (maxY downTo minY)
        }.mapIndexed { index, y ->
            point(fromX + (index * stepsXtoY).roundToInt(), y)
        }
    }
}