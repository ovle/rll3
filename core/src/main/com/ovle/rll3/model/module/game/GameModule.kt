package com.ovle.rll3.model.module.game

import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class GameModule: Module {

    override fun systems(context: Context) = listOf(
        GameSystem()
    )
}