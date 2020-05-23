package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo


interface TilesProcessor {
    fun process(levelInfo: LevelInfo, worldInfo: WorldInfo, gameEngine: Engine)
}