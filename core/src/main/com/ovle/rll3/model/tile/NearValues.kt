package com.ovle.rll3.model.tile

import com.ovle.rll3.NearTiles
import com.ovle.rll3.TileArray

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

    val asList = listOf(
        listOf(leftUpValue, upValue, rightUpValue),
        listOf(leftValue, value, rightValue),
        listOf(leftDownValue, downValue, rightDownValue)
    )
}

fun nearValues(tiles: TileArray, x: Int, y: Int): NearTiles {
    val value = tiles.get(x, y)
    val defaultValue = null

    val xLeftValid = x > 0
    val xRightValid = x < tiles.size - 1
    val yDownValid = y > 0
    val yUpValid = y < tiles.size - 1

    val upValue = if (yDownValid) tiles.get(x, y-1) else defaultValue
    val downValue = if (yUpValid) tiles.get(x, y+1) else defaultValue
    val leftValue  = if (xLeftValid) tiles.get(x-1, y) else defaultValue
    val rightValue  = if (xRightValid) tiles.get(x+1, y) else defaultValue

    val rightUpValue = if (yDownValid && xRightValid) tiles.get(x+1, y-1) else defaultValue
    val rightDownValue = if (yUpValid && xRightValid) tiles.get(x+1, y+1) else defaultValue
    val leftUpValue = if (yDownValid && xLeftValid) tiles.get(x-1, y-1) else defaultValue
    val leftDownValue = if (yUpValid && xLeftValid) tiles.get(x-1, y+1) else defaultValue

    return NearValues(x, y, value, rightValue, downValue, leftValue, upValue, rightUpValue, rightDownValue, leftUpValue, leftDownValue)
}