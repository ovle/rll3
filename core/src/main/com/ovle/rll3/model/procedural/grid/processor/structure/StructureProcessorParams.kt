package com.ovle.rll3.model.procedural.grid.processor.structure

import com.ovle.rll3.TileCondition
import com.ovle.rll3.TileTypeMapper
import com.ovle.rll3.model.procedural.grid.factory.GridFactory

data class StructureProcessorParams(
    val number: IntRange,
    val size: IntRange,
    val factory: GridFactory,
    val tileMapper: TileTypeMapper,
    val tilePreFilter: TileCondition? = null,
    val tilePostFilter: TileCondition? = null
)