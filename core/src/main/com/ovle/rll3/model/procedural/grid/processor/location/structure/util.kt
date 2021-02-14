package com.ovle.rll3.model.procedural.grid.processor.location.structure

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.TileArray
import com.ovle.rlUtil.TileCondition
import com.ovle.rlUtil.cartesianProduct
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rll3.model.util.info
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

//todo test
//todo move to rl-util
fun spawnPoint(random: Random, tiles: TileArray, structureSize: Int, tileFilter: TileCondition?, maxAttempts: Int): GridPoint2? {
    fun isTileSpawnAllowed(tiles: TileArray, x: Int, y: Int) = tileFilter?.invoke(tiles[x, y]) ?: true

    val widthDiff = tiles.size - structureSize
    val heightDiff = tiles.size - structureSize
    var result: GridPoint2? = null
    repeat(maxAttempts) {
        val x = (0..widthDiff).random(random)
        val y = (tiles.size - 1) - (0..heightDiff).random(random)

        val checkpointsX = arrayOf(x, x + (structureSize - 1)/2, x + (structureSize - 1))
        val checkpointsY = arrayOf(y, y - (structureSize - 1)/2, y - (structureSize - 1))
//        println("x: $x, y: $y")
//        println("checkpointsX: ${checkpointsX.joinToString { it.toString() }}")
//        println("checkpointsY: ${checkpointsY.joinToString { it.toString() }}")
        val checkpoints = cartesianProduct(checkpointsX, checkpointsY)

        val isSpawnAllowed = checkpoints.all { (cx, cy) -> isTileSpawnAllowed(tiles, cx, cy) }
        if (isSpawnAllowed) {
            result = point(x, y)
            return@repeat
        }
    }

    return result
}