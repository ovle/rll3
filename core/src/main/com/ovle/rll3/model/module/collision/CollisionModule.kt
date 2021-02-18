package com.ovle.rll3.model.module.collision

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module

class CollisionModule: Module {

    override fun templateComponents(name: String): List<ComponentFactory> {
        return when (name) {
            "collision" -> listOf {
                    value ->
                CollisionComponent(
                    passable4Body = value?.get("passable4Body") as Boolean? ?: false,
                    passable4Light = value?.get("passable4Light") as Boolean? ?: true
                )
            }
            else -> emptyList()
        }
    }
}