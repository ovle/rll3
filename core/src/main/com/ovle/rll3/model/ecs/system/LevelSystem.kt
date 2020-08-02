package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.ConnectionId
import com.ovle.rll3.TileType
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.dto.LevelDescription
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.component.special.LevelComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.level
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.move
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.procedural.config.levelParams
import com.ovle.rll3.model.procedural.grid.processor.LevelConnectionProcessor
import com.ovle.rll3.model.tile.*
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.nearExclusive
import ktx.ashley.get
import kotlin.random.Random


class LevelSystem: EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<WorldInit> { loadFirstLevel() }
        EventBus.subscribe<EntityLevelTransition> { loadNextLevel(levelInfo(), it.connectionId) }
    }

    private fun loadFirstLevel(): LevelInfo {
        val worldInfo = worldInfo()
        val playerInfo = playerInfo()
        val nextLevel = changeLevel(null, null, worldInfo)
        return initLevelEntities(nextLevel, null, worldInfo, playerInfo)
    }

    private fun loadNextLevel(oldLevel: LevelInfo, connectionId: ConnectionId): LevelInfo {
        val oldConnection = oldLevel.entities.connection(connectionId)!![levelConnection]!!

        storeEntities(oldLevel)

        val worldInfo = worldInfo()
        val playerInfo = playerInfo()
        val nextLevel= changeLevel(oldLevel, oldConnection, worldInfo)
        return initLevelEntities(nextLevel, oldConnection, worldInfo, playerInfo)
    }


    private fun initLevelEntities(newLevel: LevelInfo, oldConnection: LevelConnectionComponent?, worldInfo: WorldInfo, playerInfo: PlayerInfo): LevelInfo {
        val entities = allEntities().toList()
        var levelEntity = entityWith(entities, LevelComponent::class)

        if (levelEntity == null) levelEntity = newLevel(newLevel, engine)!!

        levelEntity[level]?.level = newLevel

        val interactionEntity = newPlayerInteraction(engine)

        //setVisited(connectionId, level)
        setVisited(oldConnection, newLevel)

        val newLevelDescription = newLevel.description

        send(LevelLoaded(newLevel, levelParams(newLevelDescription)))

        newLevel.entities.forEach {
            send(EntityInitialized(it))
        }

        return newLevel
    }

    private fun resetEntity(entity: Entity, startPosition: GridPoint2) {
        entity[position]!!.gridPosition = startPosition
        entity[move]?.path?.reset()

        //todo event
    }

    private fun changeLevel(oldLevel: LevelInfo?, oldConnection: LevelConnectionComponent?, worldInfo: WorldInfo): LevelInfo {
        val newLevelDescriptionId = oldConnection?.levelDescriptionId ?: worldInfo.entryPoint
        val newLevelDescription = levelDescription(newLevelDescriptionId, worldInfo)

        val storedLevel = LevelRegistry.levelInfoByDesciption(newLevelDescriptionId)?.also {
//            println("load cached level: ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            restoreEntities(it)
        }
        val newLevel = storedLevel ?: newLevelInfo(newLevelDescription, worldInfo).also {
//            println("create new level ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            val postProcessors = levelParams(newLevelDescription).postProcessors + LevelConnectionProcessor()
            postProcessors.forEach {
                processor ->
                processor.process(it, worldInfo, engine)
            }
        }

        LevelRegistry.addLevel(newLevel)

        oldConnection?.let {
            if (it.backConnectionId == null) {
                val newConnection = backConnection(newLevel, oldLevel!!)
                it.backConnectionId = newConnection.id
                newConnection.backConnectionId = oldConnection.id
            }
        }

        return newLevel
    }

    private fun levelParams(levelDescription: LevelDescription) = levelParams.getValue(levelDescription.params)

    private fun setVisited(connection: LevelConnectionComponent?, level: LevelInfo?) {
        if (connection == null) return
        if (level == null) return

        val connections = entitiesWith(level.entities, LevelConnectionComponent::class)
        val backConnection = connections.find { it[levelConnection]!!.id == connection.backConnectionId }!!
        backConnection[levelConnection]!!.visited = true
    }

    /*
fun dungeonGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= DungeonGridFactory.wallTreshold -> wallTileId
        gridValue == DungeonGridFactory.floorTreshold -> groundTileId
        gridValue == DungeonGridFactory.corridorTreshold -> corridorTileId
        else -> throw RuntimeException("illegal tile value: $gridValue")
    }
}

fun caveGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= CelullarAutomataGridFactory.wallMarker -> wallTileId
        gridValue >= CelullarAutomataGridFactory.pitMarker -> pitFloorTileId
        else -> groundTileId //todo
    }
}

fun villageGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= NoiseGridFactory.wallTreshold -> wallTileId
        gridValue >= NoiseGridFactory.floorTreshold -> groundTileId
        else -> waterTileId //todo
    }
}
    */

    private fun gridValueToTileType(value: Float): TileType {
        return when {
            value >= 0.8 -> wallTileId
            value >= 0.5 -> groundTileId
            else -> waterTileId
        }
    }

    private fun newLevelInfo(levelDescription: LevelDescription, worldInfo: WorldInfo): LevelInfo {
        val levelParams = levelParams(levelDescription)
        val factoryParams = levelParams.factoryParams
        val grid = levelParams.gridFactory.get(factoryParams, worldInfo)
        val tiles = gridToTileArray(grid, ::gridValueToTileType)
        val id = randomId()

        return LevelInfo(
            id = id,
            tiles = tiles,
            description = levelDescription
        )
    }

    private fun backConnection(newLevel: LevelInfo, oldLevel: LevelInfo): LevelConnectionComponent {
        val connections = entitiesWith(newLevel.entities, LevelConnectionComponent::class)
            .map { it[levelConnection]!! }

        return connections.find { it.levelDescriptionId == oldLevel.description.id }!!
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun playerStartPosition(newLevel: LevelInfo, oldConnection: LevelConnectionComponent?, r: Random): GridPoint2 {
        val connections = entitiesWith(newLevel.entities, LevelConnectionComponent::class)
        check(connections.isNotEmpty())

        val tiles = newLevel.tiles
        if (oldConnection == null) {
            var firstStartPosition: GridPoint2? = null
            val playerSpawnPoints = newLevel.structures.flatMap { it.template.playerSpawns }
            if (playerSpawnPoints.isNotEmpty()) {
                firstStartPosition = playerSpawnPoints.randomOrNull(r)?.apply { y = tiles.size - y - 1 }
            }
            if (firstStartPosition == null) {
                firstStartPosition = tiles.positions().filter { tiles.isPassable(it) }.random(r)
            }

            return firstStartPosition
        }

        val newConnection = connections.find { it[levelConnection]!!.id == oldConnection.backConnectionId }!!
        val newConnectionPosition = newConnection[position]?.gridPosition!!
        val entityPositions = newLevel.entities.positions()
        val nearPoints = newConnectionPosition.nearExclusive()
            .filter { tiles.isPointValid(it.x, it.y) && tiles.isPassable(it) && it !in entityPositions } //todo
        check(nearPoints.isNotEmpty())

        return nearPoints.random(r)
    }

    private fun storeEntities(level: LevelInfo) {
        val storedEntities = LevelRegistry.store(level.id)
        storedEntities.forEach { entity ->
            engine.removeEntity(entity)
        }
    }

    private fun restoreEntities(level: LevelInfo) {
        val restoredEntities = LevelRegistry.restore(level.id)
        restoredEntities?.forEach {
            val entity = engine.entity(it.id, *it.components.toTypedArray())
            level.entities.add(entity)
        }
    }
}