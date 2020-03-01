package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.entity.newTrigger
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.DungeonGenerationSettings
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTileId
import com.ovle.rll3.point

//todo more complex trap types
class TrapProcessor : TilesProcessor {

    override fun process(tiles: TileArray, generationSettings: LevelGenerationSettings, gameEngine: Engine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        generationSettings as DungeonGenerationSettings

        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)
                val isRoomFloorTile = nearTiles.value?.typeId == roomFloorTileId
                val isFreeForTrap = isRoomFloorTile
                val isTrap = isFreeForTrap && Math.random() <= generationSettings.trapChance
                if (isTrap) {
                    result.add(newTrigger(point(x, y), gameEngine))
                }
            }
        }
        return result
    }
}