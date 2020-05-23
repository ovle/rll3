package com.ovle.rll3.model.procedural.grid.processor

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.special.LevelInfo
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.newConnection
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.tile.isPassable
import com.ovle.rll3.point
import ktx.ashley.get
import kotlin.random.Random


class LevelConnectionProcessor: TilesProcessor {

    override fun process(levelInfo: LevelInfo, worldInfo: WorldInfo, gameEngine: Engine) {
        val levelDescription = levelInfo.description
        val r = worldInfo.r

        val connections = levelDescription.connections
        val enterConnections = worldInfo.levels
            .filter { levelDescription.id in it.connections }
            .map { it.id }

        val candidatePositions = candidatePositions(levelInfo)
        val result= mutableListOf<Entity>()

        val enterConnectionType = LevelConnectionType.Up //doesn't depend on how do we enter this level, only on world config
        result += fillConnections(enterConnectionType, candidatePositions, enterConnections, gameEngine, r)
        result += fillConnections(enterConnectionType.opposite(), candidatePositions, connections, gameEngine, r)

        levelInfo.entities.addAll(result)
    }

    private fun candidatePositions(levelInfo: LevelInfo): MutableList<GridPoint2> {
        val tiles = levelInfo.tiles
        val claimed = levelInfo.entities.mapNotNull { it[Mappers.position]?.gridPosition }.toSet()
        val candidatePositions = mutableListOf<GridPoint2>()
        for (x in 0 until tiles.size) {
            for (y in 0 until tiles.size) {
                val point = point(x, y)
                if (point in claimed) continue

                val isCandidate = tiles.isPassable(point)
                if (isCandidate) candidatePositions.add(point)
            }
        }
        return candidatePositions
    }

    private fun fillConnections(connectionType: LevelConnectionType, candidatePositions: MutableList<GridPoint2>, connections: List<LevelDescriptionId>, gameEngine: Engine, r: Random): MutableList<Entity> {
        val result = mutableListOf<Entity>()
        var attempts = 0
        val maxAttempts = 20
        val connectionsToHave = connections.size
        val tempConnections = connections.toMutableList()

        while (result.size < connectionsToHave) {
            if (attempts >= maxAttempts) break
            if (candidatePositions.isEmpty()) break

            val position = candidatePositions.random(r)
            candidatePositions.remove(position)

            val descriptionId = tempConnections.random(r)
            tempConnections.remove(descriptionId)

            val id = randomId()
            result.add(newConnection(id, position, gameEngine, connectionType, descriptionId))

            attempts++
        }

        return result
    }
}