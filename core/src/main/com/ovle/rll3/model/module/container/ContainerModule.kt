package com.ovle.rll3.model.module.container

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class ContainerModule: Module {

    override fun systems(context: Context) = listOf(
//        ContainerSystem()
        CarrierSystem()
    )

    override fun templateComponents(name: String): List<ComponentFactory> {
        return when (name) {
            "container" -> listOf { _ -> ContainerComponent() }
            "carrier" -> listOf { _ -> CarrierComponent() }
            else -> emptyList()
        }
    }
}