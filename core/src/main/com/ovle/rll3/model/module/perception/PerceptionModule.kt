package com.ovle.rll3.model.module.perception

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class PerceptionModule: Module {

    override fun systems(context: Context) = listOf(
        PerceptionSystem()
    )

    override fun templateComponents(name: String): List<ComponentFactory> = when (name) {
        "perception" -> listOf { value ->
            PerceptionComponent(value!!["sight"] as Int? ?: 5)
        }
        else -> emptyList()
    }
}