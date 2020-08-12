package com.ovle.rll3.model.procedural.grid.processor.structure

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.TileArray
import com.ovle.rll3.TileCondition
import com.ovle.rll3.cartesianProduct
import com.ovle.rll3.point
import kotlin.random.Random

fun TileArray.setArray(index: GridPoint2, value: TileArray, tilePostFilter: TileCondition?) {
    (0 until value.size).forEach { i ->
        (0 until value.size).forEach { j ->
            val x = index.x + j
            val y = index.y - i
            if (tilePostFilter?.invoke(get(x, y)) != false) {
                set(x, y, value[i, j])
            }
        }
    }
}

fun spawnPoint(random: Random, tiles: TileArray, structureSize: Int, tileFilter: TileCondition?, maxAttempts: Int): GridPoint2? {
    fun isTileSpawnAllowed(tiles: TileArray, x: Int, y: Int) = tileFilter?.invoke(tiles[x, y]) ?: true

    val widthDiff = tiles.size - structureSize
    val heightDiff = tiles.size - structureSize
    var result: GridPoint2? = null
    repeat(maxAttempts) {
        val x = (0..widthDiff).random(random)
        val y = (tiles.size - 1) - (0..heightDiff).random(random)

        val checkpointsX = arrayOf(x, x + structureSize/2, x + structureSize)
        val checkpointsY = arrayOf(y, y - structureSize/2, y - structureSize)
        val checkpoints = cartesianProduct(checkpointsX, checkpointsY)
        val isSpawnAllowed = checkpoints.all { (cx, cy) -> isTileSpawnAllowed(tiles, cx, cy) }
        if (isSpawnAllowed) {
            result = point(x, y)
            return@repeat
        }
    }

    return result
}