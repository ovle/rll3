package com.ovle.rll3.model.ai.bt

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.TileArray
import com.ovle.rll3.model.module.core.entity.randomId
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.location.lowGroundTileId
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.point

val tiles = arrayOf(
    lowGroundTileId, lowGroundTileId, lowGroundTileId,
    lowGroundTileId, lowGroundTileId, lowGroundTileId,
    lowGroundTileId, lowGroundTileId, lowGroundTileId
)

val random = RandomParams(0L)
val worldInfo = WorldInfo(randomId(), "", random, TileArray(arrayOf(), 0), Grid(0), Grid(0), listOf())
val locationInfo = LocationInfo(randomId(), point(0, 0), random, TileArray(tiles, 3))
