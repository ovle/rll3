package com.ovle.rll3.model.procedural.grid

import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.TilesInfo

//todo
val dungeonGenerationSettings = DungeonGenerationSettings(
    roomTypes = arrayOf(RoomType.DefaultRoomType.SQUARE, RoomType.DefaultRoomType.ROUNDED, RoomType.DefaultRoomType.CASTLE, RoomType.DefaultRoomType.DIAMOND),
    maxRoomSize = 15,
    minRoomSize = 5,
    tolerance = 5,
    windingChance = 0.25f,
    randomConnectorChance = 0.05f
)

/**
 *
 */
fun createTiles(size: Int, gridFactory: GridFactory, mapper: GridMapper<TileArray>): TilesInfo {
        val grid = gridFactory.get(size, dungeonGenerationSettings)
        return mapper.map(grid)
            .run {
                    val result = TilesInfo(tiles = this)

                    RoomsInfoPostProcessor().process(result)   //todo
                    DoorTilesInfoPostProcessor().process(result)   //todo
                    LightSourceTilesInfoPostProcessor().process(result)   //todo
                    RoomStructurePostProcessor().process(result)

                    result
            }
}

/**
 *
 */
fun <T> createTiles(size: Int, factory: TileFactory) = factory.get(size)
