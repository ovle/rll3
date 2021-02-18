package com.ovle.rll3.model.module.tile

import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class TileModule: Module {

    override fun systems(context: Context) = listOf(
        TileSystem()
    )
}