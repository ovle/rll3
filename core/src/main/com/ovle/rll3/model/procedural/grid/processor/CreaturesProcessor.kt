package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LevelDescription
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.entity.newCreature
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.DungeonLevelFactoryParams
import com.ovle.rll3.model.procedural.grid.floorTypes
import com.ovle.rll3.model.tile.corridorFloorTileId
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTileId
import com.ovle.rll3.point

class CreaturesProcessor : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine, levelDescription: LevelDescription) {
        val tiles = levelInfo.tiles
        val creatures = mutableListOf<Entity>()

        val factoryParams = levelDescription.params.factoryParams
        factoryParams as DungeonLevelFactoryParams

        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)

                val isFloorTile = nearTiles.value?.typeId in floorTypes
                val isCorridorFloor = nearTiles.value?.typeId == corridorFloorTileId
                val isRoomFloor = nearTiles.value?.typeId == roomFloorTileId

                //todo

                if (isRoomFloor && Math.random() <= factoryParams.creatureChance) {
                    creatures.add(newCreature(point(x, y), gameEngine))
                }
            }
        }

        levelInfo.objects.plusAssign(creatures)
    }
}