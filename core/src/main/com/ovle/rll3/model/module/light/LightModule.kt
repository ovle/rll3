package com.ovle.rll3.model.module.light

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module

class LightModule: Module {

    override fun templateComponents(name: String): List<ComponentFactory> = when (name) {
        "light" -> listOf { value ->
            LightSourceComponent(AOEData(value!!["radius"] as Int))
        }
        else -> emptyList()
    }
}