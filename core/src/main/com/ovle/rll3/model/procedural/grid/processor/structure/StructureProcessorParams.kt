package com.ovle.rll3.model.procedural.grid.processor.structure

import com.ovle.rll3.TileCondition
import com.ovle.rll3.TileMapper1
import com.ovle.rll3.model.procedural.grid.GridFactory

data class StructureProcessorParams(
    val number: Int,
    val size: Int,
    val factory: GridFactory,
    val tileMapper: TileMapper1,
    val tilePreFilter: TileCondition? = null,
    val tilePostFilter: TileCondition? = null
)