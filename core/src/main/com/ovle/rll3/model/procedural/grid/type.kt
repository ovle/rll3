package com.ovle.rll3.model.procedural.grid

import com.badlogic.ashley.core.Engine
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.game.LevelInfo
import com.ovle.rll3.model.procedural.config.RandomParams


interface LevelProcessor {
    fun process(levelInfo: LevelInfo, gameEngine: Engine)
}

interface GridFactory {
    fun get(random: RandomParams): Grid
}

interface GridProcessor {
    fun process(grid: Grid, random: RandomParams)
}