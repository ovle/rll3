package com.ovle.rll3.model.module.resource

import com.ovle.rll3.ComponentData
import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class ResourceModule: Module {

    override fun systems(context: Context) = listOf(
        ResourceSystem()
    )

    override fun templateComponents(name: String): List<ComponentFactory> {
        fun resourceType(value: ComponentData?) = ResourceType.valueOf((value!!["type"] as String).capitalize())

        return when (name) {
            "source" -> listOf { value ->
                SourceComponent(
                    type = resourceType(value),
                    amount = value!!["amount"] as ResourceAmount
                )
            }
            "resource" -> listOf { value ->
                ResourceComponent(type = resourceType(value))
            }
            else -> emptyList()
        }
    }
}