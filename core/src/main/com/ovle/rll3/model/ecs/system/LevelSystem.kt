package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Event
import com.ovle.rll3.Event.LevelLoaded
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.LevelComponent
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.ecs.component.Mappers.animation
import com.ovle.rll3.model.ecs.component.Mappers.level
import com.ovle.rll3.model.ecs.component.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.Mappers.move
import com.ovle.rll3.model.ecs.component.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.component.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.ecs.system.level.ConnectionData
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.ecs.system.level.LevelTransitionInfo
import com.ovle.rll3.model.procedural.config.LevelSettings
import com.ovle.rll3.model.procedural.config.caveLevelSettings
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.point
import ktx.ashley.get


class LevelSystem: EventSystem<Event>() {

    override fun channel() = receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is Event.GameStartedEvent -> loadLevel()
            is Event.EntityLevelTransition -> loadLevel(levelInfo(), event.connectionId)
        }
    }


    private fun loadLevel(oldLevel: LevelInfo? = null, connectionId: ConnectionId? = null): LevelInfo {
        val levelSettings = caveLevelSettings
//        val levelSettings = dungeonLevelSettings

        val connection = connection(oldLevel, connectionId)
        val connectionType = connection?.get(levelConnection)?.type ?: LevelConnectionType.Down

        if (oldLevel != null) storeEntities(oldLevel)

        val newTransition = changeLevel(oldLevel, connectionId, connectionType, engine, levelSettings)
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
        send(LevelLoaded(newLevel, levelSettings))

        return newLevel
    }

    private fun resetEntity(entity: Entity, startPosition: GridPoint2) {
        entity[position]!!.position = floatPoint(startPosition)
        entity[move]?.path?.reset()
        entity[animation]?.stopAnimation("walk")
    }

    private fun changeLevel(level: LevelInfo?, connectionId: ConnectionId?, connectionType: LevelConnectionType, engine: Engine, levelSettings: LevelSettings): LevelTransitionInfo {
        val storedConnection = LevelRegistry.connection(connectionId)
        var backConnectionId: ConnectionId? = null
        val isNewLevel = storedConnection == null

        val newLevel = if (!isNewLevel) {
            println("load cached level ${storedConnection!!.levelId}, transition: $connectionId")
            backConnectionId = storedConnection.connectionId
            LevelRegistry.levelInfo(storedConnection.levelId)
        } else {
            val newLevel = newLevelInfo(levelSettings)

            levelSettings.postProcessors.forEach {
                it.process(newLevel, levelSettings.generationSettings, engine)
            }

            println("create new level ${newLevel.id}")
            println("                        transition: $connectionId")
            LevelRegistry.addLevel(newLevel)
            if (connectionId != null) {
                val backConnectionType = connectionType.opposite()
                backConnectionId = randomConnection(newLevel, backConnectionType)
                LevelRegistry.addConnection(connectionId, ConnectionData(newLevel.id, backConnectionId))
                if (level != null) {
                    LevelRegistry.addConnection(backConnectionId, ConnectionData(level.id, connectionId))
                    println("                   back transition: $backConnectionId")
                }
            }
            newLevel
        }

        return LevelTransitionInfo(newLevel, backConnectionId, isNewLevel)
    }

    private fun setVisited(connectionId: ConnectionId?, level: LevelInfo?) {
        if (connectionId == null) return
        if (level == null) return

        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
        val connection = connections.find { it[levelConnection]!!.id == connectionId }!!
        connection[levelConnection]!!.visited = true
    }

    private fun newLevelInfo(levelSettings: LevelSettings): LevelInfo {
        val generationSettings = levelSettings.generationSettings
        val grid = levelSettings.gridFactory.get(
            generationSettings.size,
            generationSettings
        )

        val tiles = gridToTileArray(grid, levelSettings.gridValueToTileType)
        return LevelInfo(tiles = tiles)
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