package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.tile.TileArray


//todo need rework
interface TilesInfoProcessor {

    fun process(levelInfo: LevelInfo, gameEngine: Engine) {
        levelInfo.objects.plusAssign(process(levelInfo.tiles, gameEngine))
    }

    fun process(tiles: TileArray, gameEngine: Engine): Collection<Entity> {
        throw UnsupportedOperationException("")
    }
}