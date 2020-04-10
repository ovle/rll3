package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.special.LevelDescription
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.entity.levelDescription
import com.ovle.rll3.model.ecs.entity.newConnection
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.tile.nearValues
import com.ovle.rll3.point



class LevelConnectionProcessor {

    fun process(levelInfo: LevelInfo, gameEngine: Engine, worldInfo: WorldInfo) {
        val levelDescriptionId = levelInfo.descriptionId
        val levelDescription = levelDescription(levelDescriptionId, worldInfo)

        val connections = levelDescription.connections
        val enterConnections = worldInfo.levels
            .filter { levelDescriptionId in it.connections }
            .map { it.id }

        val candidatePositions = candidatePositions(levelInfo, levelDescription)

        val result= mutableListOf<Entity>()

        val enterConnectionType = LevelConnectionType.Up //doesn't depend on how do we enter this level, only on world config
        result += fillConnections(enterConnectionType, candidatePositions, enterConnections, gameEngine)
        result += fillConnections(enterConnectionType.opposite(), candidatePositions, connections, gameEngine)

        levelInfo.objects.addAll(result)
    }

    private fun candidatePositions(levelInfo: LevelInfo, levelDescription: LevelDescription): MutableList<GridPoint2> {
        val tiles = levelInfo.tiles
        val candidatePositions = mutableListOf<GridPoint2>()
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val nearTiles = nearValues(tiles, x, y)
                val isCandidate = levelDescription.params.isCellCandidateForConnection(nearTiles)

                if (isCandidate) candidatePositions.add(point(x, y))
            }
        }
        return candidatePositions
    }

    private fun fillConnections(connectionType: LevelConnectionType, candidatePositions: MutableList<GridPoint2>, connections: List<LevelDescriptionId>, gameEngine: Engine): MutableList<Entity> {
        val result = mutableListOf<Entity>()
        var attempts = 0
        val maxAttempts = 20
        val connectionsToHave = connections.size
        val tempConnections = connections.toMutableList()

        while (result.size < connectionsToHave) {
            if (attempts >= maxAttempts) break
            if (candidatePositions.isEmpty()) break

            val position = candidatePositions.random()
            candidatePositions.remove(position)

            val descriptionId = tempConnections.random()
            tempConnections.remove(descriptionId)

            result.add(newConnection(position, gameEngine, connectionType, descriptionId))

            attempts++
        }

        return result
    }
}