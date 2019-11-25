package com.ovle.rll3.model.procedural.grid

/**
 *
 */
fun <T> createTiles(size: Int, gridFactory: GridFactory, mapper: GridMapper<T>): T = mapper.map(gridFactory.get(size))

/**
 *
 */
fun <T> createTiles(size: Int, factory: TileFactory) = factory.get(size)
