package com.ovle.rll3.model.procedural.config

import com.ovle.rlUtil.TileMapper2
import com.ovle.rlUtil.noise4j.grid.factory.GridFactory
import com.ovle.rll3.model.procedural.grid.LocationProcessor
import com.ovle.rll3.model.procedural.grid.WorldProcessor

data class LocationGenerationParams(
    val templateName: String,
    val heightMapFactory: GridFactory,
    val postProcessors: Array<LocationProcessor>,
    val tileMapper: TileMapper2
)

data class WorldGenerationParams(
    val templateName: String,
    val heightMapFactory: GridFactory,
    val heatMapFactory: GridFactory,
    val postProcessors: Array<WorldProcessor>,
    val tileMapper: TileMapper2
)