package com.ovle.rll3.model.module.interaction

import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class InteractionModule: Module {

    override fun systems(context: Context) = listOf(
        BaseInteractionSystem(),
        EntityInteractionSystem(),
        TileInteractionSystem()
    )
}