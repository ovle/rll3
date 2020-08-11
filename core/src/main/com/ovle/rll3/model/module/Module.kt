package com.ovle.rll3.model.module

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.ComponentFactory
import ktx.inject.Context

//todo events
//todo ComponentMappers
interface GameModule {
    fun systems(context: Context): List<EntitySystem>
    fun components(): Map<String, ComponentFactory>
}