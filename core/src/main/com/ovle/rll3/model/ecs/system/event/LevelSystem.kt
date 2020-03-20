package com.ovle.rll3.model.ecs.system.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.Event
import com.ovle.rll3.Event.LevelLoaded
import com.ovle.rll3.EventBus.receive
import com.ovle.rll3.EventBus.send
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.component.Mappers.level
import com.ovle.rll3.model.ecs.component.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.Mappers.move
import com.ovle.rll3.model.ecs.component.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.Mappers.position
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.ecs.system.event.level.ConnectionId
import com.ovle.rll3.model.ecs.system.event.level.LevelRegistry
import com.ovle.rll3.model.procedural.grid.processor.LevelConnectionProcessor
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
        val nextLevel = changeLevel(null, null, worldInfo)
        return initLevelEntities(nextLevel, null, worldInfo)
    }

    private fun loadNextLevel(oldLevel: LevelInfo, connectionId: ConnectionId): LevelInfo {
        val oldConnection = connection(oldLevel, connectionId)!![levelConnection]!!

        storeEntities(oldLevel)

        val worldInfo = worldInfo()
        val nextLevel= changeLevel(oldLevel, oldConnection, worldInfo)
        return initLevelEntities(nextLevel, oldConnection, worldInfo)
    }


    private fun initLevelEntities(newLevel: LevelInfo, oldConnection: LevelConnectionComponent?, worldInfo: WorldInfo): LevelInfo {
        val entities = allEntities().toList()
        var levelEntity = entityWith(entities, LevelComponent::class)

        if (levelEntity == null) levelEntity = newLevel(newLevel, engine)!!

        var playerEntity: Entity? = null
        var interactionEntity = entityWith(entities, PlayerInteractionComponent::class)

        if (interactionEntity != null) playerEntity = interactionEntity[playerInteraction]?.controlledEntity
        if (playerEntity == null) playerEntity = newPlayer(engine)!!
        val startPosition = playerStartPosition(newLevel, oldConnection)
        resetEntity(playerEntity, startPosition)

        if (interactionEntity == null) interactionEntity = newPlayerInteraction(playerEntity, engine)

        levelEntity[level]?.level = newLevel

        //setVisited(connectionId, level)
        setVisited(oldConnection, newLevel)

        //send(LevelUnloaded(it))   todo
        val newLevelDescription = levelDescription(newLevel.descriptionId, worldInfo)
        send(LevelLoaded(newLevel, newLevelDescription.params, playerEntity))

        return newLevel
    }

    private fun resetEntity(entity: Entity, startPosition: GridPoint2) {
        entity[position]!!.position = floatPoint(startPosition)
        entity[move]?.path?.reset()

        //todo event
    }

    //todo case with multiple enter connections - go first, go back, go second - no storedConnection for already visited level
    //todo for the circular path - go down using one path, go up using another enter
    private fun changeLevel(oldLevel: LevelInfo?, oldConnection: LevelConnectionComponent?, worldInfo: WorldInfo): LevelInfo {
        val connectionId = oldConnection?.id
        val newLevelDescriptionId = oldConnection?.levelDescriptionId ?: worldInfo.entryPoint
        val newLevelDescription = levelDescription(newLevelDescriptionId, worldInfo)

        val storedLevel = LevelRegistry.levelInfoByDesciption(newLevelDescriptionId)?.also {
            println("load cached level: ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            restoreEntities(it)
        }
        val newLevel = storedLevel ?: newLevelInfo(newLevelDescription).also {
            println("create new level ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            newLevelDescription.params.postProcessors.forEach {
                processor ->
                processor.process(it, engine, newLevelDescription)
            }
            LevelConnectionProcessor().process(it, engine, worldInfo)
        }

        LevelRegistry.addLevel(newLevel)

        oldConnection?.let {
            if (it.backConnectionId == null) {
                val newConnection = backConnection(newLevel, oldLevel!!)
                it.backConnectionId = newConnection.id
                newConnection.backConnectionId = oldConnection.id
            }
            println("back transition: ${it.backConnectionId}")
        }

        return newLevel
    }

    private fun setVisited(connection: LevelConnectionComponent?, level: LevelInfo?) {
        if (connection == null) return
        if (level == null) return

        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
        val backConnection = connections.find { it[levelConnection]!!.id == connection.backConnectionId }!!
        backConnection[levelConnection]!!.visited = true
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

    private fun backConnection(newLevel: LevelInfo, oldLevel: LevelInfo): LevelConnectionComponent {
        val connections = entitiesWith(newLevel.objects, LevelConnectionComponent::class)
            .map { it[levelConnection]!! }

        return connections.find { it.levelDescriptionId == oldLevel.descriptionId }!!
    }

    private fun playerStartPosition(newLevel: LevelInfo, oldConnection: LevelConnectionComponent?): GridPoint2 {
        val connections = entitiesWith(newLevel.objects, LevelConnectionComponent::class)
        check(connections.isNotEmpty())

        if (oldConnection == null) {
            //todo should be far away from any connection?
            val anyConnection = connections.random()
            return point(anyConnection[position]?.position!!).apply { y -= 1 }
        }

        val newConnection = connections.find { it[levelConnection]!!.id == oldConnection.backConnectionId }!!
        val newConnectionPosition = newConnection[position]?.position!!

        return point(newConnectionPosition).apply { y -= 1 }
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