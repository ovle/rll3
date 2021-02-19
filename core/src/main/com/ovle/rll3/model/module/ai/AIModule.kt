package com.ovle.rll3.model.module.ai

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class AIModule: Module {

    override fun systems(context: Context) = listOf(
        AISystem()
    )

    override fun templateComponents(name: String): List<ComponentFactory> {
        return when (name) {
            "ai" -> listOf { value -> AIComponent(behavior = (value?.get("behavior") as String?) ?: "base") }
            else -> emptyList()
        }
    }
}