package com.ovle.rll3.model.module.render

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class RenderModule: Module {

    override fun systems(c: Context) = listOf(
        CameraSystem(c.inject()),
        RenderLocationSystem(c.inject(), c.inject(), c.inject()),
        RenderObjectsSystem(c.inject(), c.inject()),
        AnimationSystem(),
    )

    override fun templateComponents(name: String): List<ComponentFactory> = when (name) {
        "render" -> listOf { _ ->
            RenderComponent()
        }
        else -> emptyList()
    }
}