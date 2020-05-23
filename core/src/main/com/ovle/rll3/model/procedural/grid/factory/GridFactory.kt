package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.procedural.config.LevelFactoryParams

interface GridFactory {
    fun get(factoryParams: LevelFactoryParams, worldInfo: WorldInfo): Grid
}

