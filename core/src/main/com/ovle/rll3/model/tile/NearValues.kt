package com.ovle.rll3.model.tile

import com.github.czyzby.noise4j.array.Object2dArray


class NearValues<T>(
    val x: Int, val y: Int,
    val value: T,
    val rightValue: T?, val downValue: T?, val leftValue: T?, val upValue: T?,
    val rightUpValue: T?, val rightDownValue: T?, val leftUpValue: T?, val leftDownValue: T?
) {
    val all = setOf(value, rightValue, leftValue, upValue, downValue, rightUpValue, rightDownValue, leftUpValue, leftDownValue)
    val allHV = setOf(value, rightValue, leftValue, upValue, downValue)
    val near = setOf(rightValue, leftValue, upValue, downValue, rightUpValue, rightDownValue, leftUpValue, leftDownValue)
    val nearHV = setOf(rightValue, leftValue, upValue, downValue)
    val nearH = setOf(rightValue, leftValue)
    val nearV = setOf(upValue, downValue)
}

fun <T> nearValues(values: Object2dArray<T>, x: Int, y: Int, defaultValue: T? = null): NearValues<T> {
    val value = values.get(x, y)

    val xLeftValid = x > 0
    val xRightValid = x < values.width - 1
    val yDownValid = y > 0
    val yUpValid = y < values.height - 1

    val upValue = if (yDownValid) values.get(x,y-1) else defaultValue
    val downValue = if (yUpValid) values.get(x, y+1) else defaultValue
    val leftValue  = if (xLeftValid) values.get(x-1, y) else defaultValue
    val rightValue  = if (xRightValid) values.get(x+1, y) else defaultValue

    val rightUpValue = if (yDownValid && xRightValid) values.get(x+1, y-1) else defaultValue
    val rightDownValue = if (yUpValid && xRightValid) values.get(x+1, y+1) else defaultValue
    val leftUpValue = if (yDownValid && xLeftValid) values.get(x-1, y-1) else defaultValue
    val leftDownValue = if (yUpValid && xLeftValid) values.get(x-1, y+1) else defaultValue

    return NearValues(x, y, value, rightValue, downValue, leftValue, upValue, rightUpValue, rightDownValue, leftUpValue, leftDownValue)
}