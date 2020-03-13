package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LevelDescription
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.entity.newDoor
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.DungeonLevelFactoryParams
import com.ovle.rll3.model.tile.corridorFloorTileId
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTileId
import com.ovle.rll3.point

class DoorProcessor : TilesProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine, levelDescription: LevelDescription) {
        val tiles = levelInfo.tiles
        val doors = mutableListOf<Entity>()

        val factoryParams = levelDescription.params.factoryParams
        factoryParams as DungeonLevelFactoryParams

        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)

                val isCorridorFloor = nearTiles.value?.typeId == corridorFloorTileId
                val isRoomFloorNearHorisontal = roomFloorTileId in nearTiles.nearH.mapNotNull { it?.typeId }
                val isRoomFloorNearVertical = roomFloorTileId in nearTiles.nearV.mapNotNull { it?.typeId }
                val isFreeForDoor = isCorridorFloor && (isRoomFloorNearHorisontal || isRoomFloorNearVertical)
                val isDoor = isFreeForDoor && Math.random() <= factoryParams.doorChance

                if (isDoor) {
                    doors.add(newDoor(point(x, y), gameEngine))
                }
            }
        }

        levelInfo.objects.plusAssign(doors)
    }
}