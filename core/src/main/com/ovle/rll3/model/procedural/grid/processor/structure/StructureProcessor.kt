package com.ovle.rll3.model.procedural.grid.processor.structure

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.TileCondition
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.processor.TilesProcessor
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.point
import kotlin.random.Random

class StructureProcessor(private val params: StructureProcessorParams) : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine) {
        val tiles = levelInfo.tiles
        val entities = mutableListOf<Entity>()
        val random = levelInfo.random.kRandom

        val number = params.number.random(random)
        repeat(number) {
            spawnStructure(tiles, levelInfo, levelInfo.random)
        }

        levelInfo.entities.plusAssign(entities)
    }

    private fun spawnStructure(tiles: TileArray, levelInfo: LevelInfo, random: RandomParams) {
        val gridFactory = params.factory

        val structureGrid = gridFactory.get(random)
        val structureSize = params.size.random(random.kRandom)
        val point = spawnPoint(random.kRandom, tiles, structureSize) ?: return

        val structureTiles = gridToTileArray(structureGrid, params.tileMapper)

        tiles.set(point, structureTiles, params.tilePostFilter)
    }

    private fun spawnPoint(random: Random, tiles: TileArray, structureSize: Int): GridPoint2? {
        fun isTileSpawnAllowed(tiles: TileArray, x: Int, y: Int) = params.tilePreFilter?.invoke(tiles.tile(x, y)) ?: true

        val widthDiff = tiles.size - structureSize
        val heightDiff = tiles.size - structureSize
        val maxAttempts = 10
        var result: GridPoint2? = null
        repeat(maxAttempts) {
            val x = (0..widthDiff).random(random)
            val y = (tiles.size - 1) - (0..heightDiff).random(random)
            val isSpawnAllowed = isTileSpawnAllowed(tiles, x, y)
//                arrayOf(x, x + structureSize/2, x + structureSize)
//                arrayOf(y, y + structureSize/2, y + structureSize)
            if (isSpawnAllowed) {
                result = point(x, y)
                return@repeat
            }
        }

        return result
    }
}

private fun TileArray.set(index: GridPoint2, value: TileArray, tilePostFilter: TileCondition?) {
    (0 until value.size).forEach { i ->
        (0 until value.size).forEach { j ->
            val x = index.x + j
            val y = index.y - i
            if (tilePostFilter?.invoke(tile(x, y)) != false) {
                setTile(x, y, value.tile(i, j))
            }
        }
    }
}
