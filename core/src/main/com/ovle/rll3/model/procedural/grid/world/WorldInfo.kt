package com.ovle.rll3.model.procedural.grid.world

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.TileArray
import com.ovle.rll3.WorldId
import com.ovle.rll3.model.module.time.TimeInfo
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.WorldGenerationParams
import java.io.Serializable

data class WorldInfo(
    val id: WorldId,
    val name: String,
    val random: RandomParams,
    val params: WorldGenerationParams,
    val tiles: TileArray,
    val heightGrid: Grid,
    val heatGrid: Grid
): Serializable