package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.Seed
import com.ovle.rll3.model.procedural.config.LevelFactoryParams

interface GridFactory {
    fun get(params: LevelFactoryParams, seed: Seed): Grid
}

