package com.ovle.rll3.model.procedural.grid.processor.location.structure

import com.ovle.rlUtil.TileCondition
import com.ovle.rlUtil.TileMapper1
import com.ovle.rlUtil.noise4j.grid.factory.GridFactory

data class StructureProcessorParams(
    val number: Int,
    val size: Int,
    val factory: GridFactory,
    val tileMapper: TileMapper1,
    val tilePreFilter: TileCondition? = null,
    val tilePostFilter: TileCondition? = null
)