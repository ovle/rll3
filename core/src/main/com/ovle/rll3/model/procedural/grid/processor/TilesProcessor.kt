package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.ovle.rll3.model.ecs.component.LevelDescription
import com.ovle.rll3.model.ecs.component.LevelInfo


interface TilesProcessor {
    fun process(levelInfo: LevelInfo, gameEngine: Engine, levelDescription: LevelDescription)
}