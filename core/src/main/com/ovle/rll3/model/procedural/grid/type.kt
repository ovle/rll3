package com.ovle.rll3.model.procedural.grid

import com.badlogic.ashley.core.Engine
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.procedural.grid.world.WorldInfo


interface LocationProcessor {
    fun process(locationInfo: LocationInfo, gameEngine: Engine)
}

interface WorldProcessor {
    fun process(worldInfo: WorldInfo)
}