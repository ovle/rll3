package com.ovle.rll3.model.module.time

import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class TimeModule: Module {

    override fun systems(context: Context) = listOf(
        TimeSystem()
    )
}