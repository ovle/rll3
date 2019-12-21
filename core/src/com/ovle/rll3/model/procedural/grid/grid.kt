package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.procedural.dungeonGenerationSettings
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

            RoomsInfoPostProcessor().process(result, gameEngine)   //todo
            RoomStructurePostProcessor().process(result, gameEngine)
            DoorTilesInfoPostProcessor().process(result, gameEngine)   //todo
            LightSourceTilesInfoPostProcessor().process(result, gameEngine)   //todo

            result
        }
}

/**
 *
 */
fun <T> createTiles(size: Int, factory: TileFactory) = factory.get(size)
