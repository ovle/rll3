package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LevelDescription
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.WorldInfo
import com.ovle.rll3.model.tile.TileArray


//todo need rework
interface TilesProcessor {

    fun process(levelInfo: LevelInfo, gameEngine: Engine, worldInfo: WorldInfo, levelDescription: LevelDescription) {
        levelInfo.objects.plusAssign(process2(levelInfo.tiles, gameEngine, worldInfo, levelDescription))
    }

    fun process2(tiles: TileArray, gameEngine: Engine, worldInfo: WorldInfo, LevelDescription: LevelDescription): Collection<Entity> {
        throw UnsupportedOperationException("")
    }
}