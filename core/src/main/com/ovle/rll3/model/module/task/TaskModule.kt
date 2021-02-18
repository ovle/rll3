package com.ovle.rll3.model.module.task

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class TaskModule: Module {

    override fun systems(context: Context) = listOf(
        TaskSystem()
    )

    override fun templateComponents(name: String): List<ComponentFactory> {
        return when (name) {
            "task" -> listOf { _ ->
                TaskPerformerComponent()
            }
            else -> emptyList()
        }
    }
}