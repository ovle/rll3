package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.entity.newConnection
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.model.tile.roomFloorTileId
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.point

class LevelConnectionProcessor : TilesInfoProcessor {

    override fun process(levelInfo: LevelInfo, gameEngine: Engine) {
        val tiles = levelInfo.tiles
        val connectionsToHave = 2
        val maxAttempts = 10
        var attempts = 0
        val result= mutableListOf<Entity>()

        val candidatePositions = mutableListOf<GridPoint2>()
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)
                val isWall = (nearTiles.nearH + nearTiles.value)
                    .filterNotNull().all { it.typeId == wallTileId }
                val isRoomFloorNear =  nearTiles.upValue?.typeId == roomFloorTileId

                if (isWall && isRoomFloorNear) candidatePositions.add(point(x, y))
            }
        }

        while (result.size < connectionsToHave) {
            if (attempts >= maxAttempts) break
            if (candidatePositions.isEmpty()) break

            result.add(newConnection(candidatePositions.random(), gameEngine))
            attempts++
        }

        levelInfo.objects.addAll(result)
    }


}