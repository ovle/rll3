package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.DoorComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.component.floatPoint
import com.ovle.rll3.model.ecs.entity
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.tile.corridorFloorTileId
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTileId

class DoorProcessor : TilesInfoProcessor {

    override fun process(tiles: TileArray, gameEngine: Engine): Collection<Entity> {
        val result = mutableListOf<Entity>()
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)

                val isCorridorFloor = nearTiles.value?.typeId == corridorFloorTileId
                val isRoomFloorNearHorisontal = roomFloorTileId in nearTiles.nearH.mapNotNull { it?.typeId }
                val isRoomFloorNearVertical = roomFloorTileId in nearTiles.nearV.mapNotNull { it?.typeId }
                val isDoor = isCorridorFloor && (isRoomFloorNearHorisontal || isRoomFloorNearVertical)

                if (isDoor) {
                    result.add(door(x, y, gameEngine))
                }
            }
        }
        return result
    }

    private fun door(x: Int, y: Int, gameEngine: Engine): Entity {
        return gameEngine.entity(
            PositionComponent(floatPoint(x.toFloat(), y.toFloat())),
            DoorComponent()
        )
    }
}