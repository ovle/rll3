package com.ovle.rll3.model.module.core

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.ComponentFactory

//todo events
//todo ComponentMappers
interface Module {
    fun systems(): List<EntitySystem> = listOf()
    fun components(): List<Component> = listOf()
    fun templateComponents(name: String): List<ComponentFactory> = listOf()
}