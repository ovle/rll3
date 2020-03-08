package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Event
import com.ovle.rll3.Event.LevelLoaded
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.Mappers.animation
import com.ovle.rll3.model.ecs.component.Mappers.level
import com.ovle.rll3.model.ecs.component.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.Mappers.move
import com.ovle.rll3.model.ecs.component.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.ecs.system.level.ConnectionData
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.ecs.system.level.LevelTransitionInfo
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.point
import ktx.ashley.get


class LevelSystem: EventSystem<Event>() {

    override fun channel() = receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is Event.WorldInitEvent -> loadFirstLevel()
            is Event.EntityLevelTransition -> loadNextLevel(levelInfo(), event.connectionId)
        }
    }


    private fun loadFirstLevel(): LevelInfo {
        val worldInfo = worldInfo()
        val newTransition = changeLevel(null, null, engine, worldInfo)
        return initLevelEntities(newTransition, worldInfo)
    }

    private fun loadNextLevel(oldLevel: LevelInfo, connectionId: ConnectionId): LevelInfo {
        val connection = connection(oldLevel, connectionId)!!
        val connectionComponent = connection[levelConnection]!!

        storeEntities(oldLevel)

        val worldInfo = worldInfo()
        val newTransition = changeLevel(oldLevel, connectionComponent, engine, worldInfo)
        return initLevelEntities(newTransition, worldInfo)
    }


    private fun initLevelEntities(newTransition: LevelTransitionInfo, worldInfo: WorldInfo): LevelInfo {
        val newLevel = newTransition.levelInfo
        if (!newTransition.isNew) {
            restoreEntities(newLevel)
        }
        val startPosition = playerStartPosition(newLevel, newTransition.connectionId)

        val entities = allEntities().toList()
        var levelEntity = entityWith(entities, LevelComponent::class)

        if (levelEntity == null) levelEntity = newLevel(newLevel, engine)!!

        var playerEntity: Entity? = null
        var interactionEntity = entityWith(entities, PlayerInteractionComponent::class)

        if (interactionEntity != null) playerEntity = interactionEntity[playerInteraction]?.controlledEntity
        if (playerEntity == null) playerEntity = newPlayer(engine)!!
        resetEntity(playerEntity, startPosition)

        if (interactionEntity == null) interactionEntity = newPlayerInteraction(playerEntity, engine)

        levelEntity[level]?.level = newLevel

        //setVisited(connectionId, level)
        setVisited(newTransition.connectionId, newLevel)

        //send(LevelUnloaded(it))   todo
        val newLevelDescription = levelDescription(newLevel.descriptionId, worldInfo)
        send(LevelLoaded(newLevel, newLevelDescription.params))

        return newLevel
    }

    private fun resetEntity(entity: Entity, startPosition: GridPoint2) {
        entity[position]!!.position = floatPoint(startPosition)
        entity[move]?.path?.reset()
        entity[animation]?.stopAnimation("walk")
    }

    private fun changeLevel(oldLevel: LevelInfo?, connection: LevelConnectionComponent?, engine: Engine, worldInfo: WorldInfo): LevelTransitionInfo {
        val connectionId = connection?.id
        val connectionType = connection?.type ?: LevelConnectionType.Down

        val storedConnection = LevelRegistry.connection(connectionId)
        var backConnectionId: ConnectionId? = null
        val isAlreadyLoadedLevel = storedConnection != null

        //todo case with multiple enter connections - go first, go back, go second - no storedConnection for already visited level
        val newLevel = if (isAlreadyLoadedLevel) {
            println("load cached level ${storedConnection!!.levelId}, transition: $connectionId")
            backConnectionId = storedConnection.connectionId
            LevelRegistry.levelInfo(storedConnection.levelId)
        } else {
            val newLevelDescriptionId = connection?.levelDescriptionId ?: worldInfo.entryPoint
            val newLevelDescription = levelDescription(newLevelDescriptionId, worldInfo)
            val newLevel = newLevelInfo(newLevelDescription)

            val levelParams = newLevelDescription.params
            levelParams.postProcessors.forEach {
                it.process(newLevel, engine, worldInfo, newLevelDescription)
            }

            println("create new level ${newLevel.id}")
            println("                        description: ${newLevelDescription.id}")
            println("                        transition: $connectionId")
            LevelRegistry.addLevel(newLevel)
            if (connectionId != null) {
                val backConnectionType = connectionType.opposite()
                backConnectionId = randomConnection(newLevel, backConnectionType)
                LevelRegistry.addConnection(connectionId, ConnectionData(newLevel.id, backConnectionId))
                if (oldLevel != null) {
                    LevelRegistry.addConnection(backConnectionId, ConnectionData(oldLevel.id, connectionId))
                    println("                   back transition: $backConnectionId")
                }
            }
            newLevel
        }

        return LevelTransitionInfo(newLevel, backConnectionId, !isAlreadyLoadedLevel)
    }

    private fun setVisited(connectionId: ConnectionId?, level: LevelInfo?) {
        if (connectionId == null) return
        if (level == null) return

        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
        val connection = connections.find { it[levelConnection]!!.id == connectionId }!!
        connection[levelConnection]!!.visited = true
    }

    private fun newLevelInfo(levelDescription: LevelDescription): LevelInfo {
        val levelParams = levelDescription.params
        val factoryParams = levelParams.factoryParams
        val grid = levelParams.gridFactory.get(factoryParams)

        val tiles = gridToTileArray(grid, levelParams.gridValueToTileType)
        return LevelInfo(
            tiles = tiles,
            descriptionId = levelDescription.id
        )
    }

    private fun randomConnection(level: LevelInfo, type: LevelConnectionType): ConnectionId {
        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
            .map { it[levelConnection]!! }
        return connections.filter { it.type == type }.random().id
    }

    private fun playerStartPosition(level: LevelInfo, connectionId: ConnectionId?): GridPoint2 {
        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
        check(connections.isNotEmpty())

        val floatPoint =
            if (connectionId == null) connections.random()[position]?.position
            else connections.find { it[levelConnection]!!.id == connectionId }?.get(position)?.position

        return point(floatPoint!!)
    }

    private fun storeEntities(level: LevelInfo) {
        val storedEntities = LevelRegistry.store(level.id)
        println("                        storedEntities: ${storedEntities.size}")
        storedEntities.forEach { entity ->
            engine.removeEntity(entity)
        }
    }

    private fun restoreEntities(level: LevelInfo) {
        val restoredEntities = LevelRegistry.restore(level.id)
        println("                        restoredEntities: ${restoredEntities?.size}")
        restoredEntities?.forEach {
            val entity = engine.entity(*it.components.toTypedArray())
            level.objects.add(entity)
        }
    }
}