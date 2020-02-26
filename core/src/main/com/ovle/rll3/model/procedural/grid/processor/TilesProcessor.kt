package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.tile.TileArray


//todo need rework
interface TilesProcessor {

    fun process(levelInfo: LevelInfo, generationSettings: LevelGenerationSettings, gameEngine: Engine) {
        levelInfo.objects.plusAssign(process(levelInfo.tiles, generationSettings, gameEngine))
    }

    fun process(tiles: TileArray, generationSettings: LevelGenerationSettings, gameEngine: Engine): Collection<Entity> {
        throw UnsupportedOperationException("")
    }
}