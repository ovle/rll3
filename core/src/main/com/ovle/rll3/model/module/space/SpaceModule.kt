package com.ovle.rll3.model.module.space

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module


class SpaceModule: Module {

    override fun systems() = listOf(
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