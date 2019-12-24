package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.procedural.dungeonGenerationSettings
import com.ovle.rll3.model.procedural.grid.processor.*
import com.ovle.rll3.model.tile.LevelInfo
import com.ovle.rll3.model.tile.TileArray

/**
 *
 */
fun createTiles(size: Int, gridFactory: GridFactory, mapper: GridMapper<TileArray>, gameEngine: GameEngine): LevelInfo {
    val grid = gridFactory.get(size, dungeonGenerationSettings)
    return mapper.map(grid)
        .run {
            val result = LevelInfo(tiles = this)

            //todo inject list by interface
            RoomsInfoProcessor().process(result, gameEngine)
            RoomStructureProcessor().process(result, gameEngine)
            DoorProcessor().process(result, gameEngine)
            LightSourceProcessor().process(result, gameEngine)
            TrapProcessor().process(result, gameEngine)

            result
        }
}

/**
 *
 */
fun <T> createTiles(size: Int, factory: TileFactory) = factory.get(size)
