package com.ovle.rll3.model.procedural.grid

import com.badlogic.ashley.core.Engine
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.world.WorldInfo


interface LocationProcessor {
    fun process(locationInfo: LocationInfo, gameEngine: Engine)
}

interface WorldProcessor {
    fun process(worldInfo: WorldInfo)
}

interface GridFactory {
    fun get(random: RandomParams): Grid
}

interface GridProcessor {
    fun process(grid: Grid, random: RandomParams)
}