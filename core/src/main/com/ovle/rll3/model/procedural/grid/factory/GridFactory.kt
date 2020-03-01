package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings

interface GridFactory {
    fun get(size: Int, settings: LevelGenerationSettings): Grid
}

