package com.ovle.rll3.model.module.entityAction

import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class EntityActionModule: Module {

    override fun systems(context: Context) = listOf(
        EntityActionSystem()
    )

    override fun components() = listOf(
        EntityActionComponent()
    )
}