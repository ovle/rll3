package com.ovle.rll3.model.procedural.grid

import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.TilesInfo

/**
 *
 */
fun createTiles(size: Int, gridFactory: GridFactory, mapper: GridMapper<TileArray>) = mapper.map(gridFactory.get(size))
    .run {
        val result = TilesInfo(this)
        DoorsTileArrayPostProcessor().process(result)   //todo
        result
    }

/**
 *
 */
fun <T> createTiles(size: Int, factory: TileFactory) = factory.get(size)
