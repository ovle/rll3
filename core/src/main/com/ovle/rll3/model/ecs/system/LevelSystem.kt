package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Event
import com.ovle.rll3.Event.LevelLoaded
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.*
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.entity.newPlayer
import com.ovle.rll3.model.ecs.entity.newPlayerInteraction
import com.ovle.rll3.model.ecs.system.level.ConnectionData
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.ecs.system.level.LevelTransitionInfo
import com.ovle.rll3.model.procedural.dungeonGenerationSettings
import com.ovle.rll3.model.procedural.grid.DungeonGenerationSettings
import com.ovle.rll3.model.procedural.grid.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.procedural.grid.processor.*
import com.ovle.rll3.model.procedural.mapSizeInTiles
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.point
import ktx.ashley.get


class LevelSystem: EventSystem<Event>() {

    private val levelMapper: ComponentMapper<LevelComponent> = componentMapper()

    override fun channel() = receive<Event>()

    override fun dispatch(event: Event) {
        when (event) {
            is Event.GameStartedEvent -> loadLevel()
            is Event.EntityLevelTransition -> loadLevelOnTransition(event.connectionId)
        }
    }

    private fun loadLevelOnTransition(connectionId: ConnectionId) {
        val oldLevelInfo = levelInfoNullable()?.also {
            storeEntities(it)
        }

        loadLevel(oldLevelInfo!!, connectionId)
    }

    private fun loadLevel(oldLevel: LevelInfo? = null, connectionId: ConnectionId? = null): LevelInfo {
        val newTransition = changeLevel(oldLevel, connectionId, engine)
        val newLevel = newTransition.levelInfo
        if (!newTransition.isNew) {
            restoreEntities(newLevel)
        }
        val startPosition = playerStartPosition(newLevel, newTransition.connectionId)

        val entities = allEntities().toList()
        var levelEntity = entityWithNullable(entities, LevelComponent::class)
        if (levelEntity == null) {
            levelEntity = engine.entity(LevelComponent(newLevel))!!
        }

        var playerEntity: Entity? = null
        var interactionEntity = entityWithNullable(entities, PlayerInteractionComponent::class)

        if (interactionEntity != null) {
            playerEntity = interactionEntity.component(PlayerInteractionComponent::class)?.controlledEntity
        }
        if (playerEntity == null) {
            playerEntity = newPlayer(engine)
        }

        playerEntity!!.component(PositionComponent::class)!!.position = floatPoint(startPosition)

        if (interactionEntity == null) {
            interactionEntity = newPlayerInteraction(playerEntity, engine)
        }

        levelEntity[levelMapper]?.level = newLevel

        //            send(LevelUnloaded(it))
        send(LevelLoaded(newLevel))

        return newLevel
    }

    fun changeLevel(level: LevelInfo?, connectionId: ConnectionId?, engine: Engine): LevelTransitionInfo {
        val connection = LevelRegistry.connection(connectionId)
        var backConnectionId: ConnectionId? = null
        val isNewLevel = connection == null

        val newLevel = if (!isNewLevel) {
            println("load cached level ${connection!!.levelId}, transition: $connectionId")
            backConnectionId = connection.connectionId
            LevelRegistry.levelInfo(connection.levelId)
        } else {
            val newLevel = newLevelInfo(mapSizeInTiles, DungeonGridFactory(), dungeonGenerationSettings)
            postProcess(newLevel, engine)

            println("create new level ${newLevel.id}")
            println("                        transition: $connectionId")
            LevelRegistry.addLevel(newLevel)
            if (connectionId != null) {
                backConnectionId = randomConnection(newLevel)
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

    private fun newLevelInfo(size: Int, gridFactory: GridFactory, generationSettings: DungeonGenerationSettings): LevelInfo {
        val grid = gridFactory.get(size, generationSettings)
        val tiles = gridToTileArray(grid)
        return LevelInfo(tiles = tiles)
    }

    private fun randomConnection(level: LevelInfo): ConnectionId {
        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
        return connections.random().component(LevelConnectionComponent::class)!!.id
    }

    private fun postProcess(newLevel: LevelInfo, engine: Engine) {
        //todo inject list by interface
        RoomsInfoProcessor().process(newLevel, engine)
        RoomStructureProcessor().process(newLevel, engine)

        LevelConnectionProcessor().process(newLevel, engine)
        DoorProcessor().process(newLevel, engine)
        LightSourceProcessor().process(newLevel, engine)
//                TrapProcessor().process(newLevel, engine)
    }

    private fun playerStartPosition(level: LevelInfo, connectionId: ConnectionId?): GridPoint2 {
        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
        check(connections.isNotEmpty())

        val floatPoint =
            if (connectionId == null) connections.random().component(PositionComponent::class)?.position
            else connections.find { it.component(LevelConnectionComponent::class)!!.id == connectionId }?.component(PositionComponent::class)?.position

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