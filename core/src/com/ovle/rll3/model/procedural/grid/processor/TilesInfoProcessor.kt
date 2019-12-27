package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.GameEngine
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.tile.TileArray


//todo need rework
interface TilesInfoProcessor {

    fun process(levelInfo: LevelInfo, gameEngine: GameEngine) {
        levelInfo.objects.plusAssign(process(levelInfo.tiles, gameEngine))
    }

    fun process(tiles: TileArray, gameEngine: GameEngine): Collection<Entity> {
        throw UnsupportedOperationException("")
    }
}