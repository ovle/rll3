package com.ovle.rll3.model.module.controls

import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class ControlsModule: Module {

    override fun systems(context: Context) = listOf(
        PlayerControlsSystem()
    )
}