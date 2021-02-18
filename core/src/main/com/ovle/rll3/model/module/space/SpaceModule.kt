package com.ovle.rll3.model.module.space

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class SpaceModule: Module {

    override fun systems(context: Context) = listOf(
        MoveSystem()
    )

    override fun components() = listOf(
        PositionComponent()
    )

    override fun templateComponents(name: String): List<ComponentFactory> {
        return when (name) {
            "move" -> listOf { _ ->
                MoveComponent()
            }
            else -> emptyList()
        }
    }
}