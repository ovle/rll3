package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.Seed
import com.ovle.rll3.model.procedural.config.LevelFactoryParams
import com.ovle.rll3.model.procedural.config.RandomParams

interface GridFactory {
    fun get(random: RandomParams): Grid
}

