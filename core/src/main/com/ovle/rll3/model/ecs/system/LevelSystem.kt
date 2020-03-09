package com.ovle.rll3.model.ecs.system

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
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
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
        val (nextLevel, connectionId) = changeLevel(null, worldInfo)
        return initLevelEntities(nextLevel, connectionId, worldInfo)
    }

    private fun loadNextLevel(oldLevel: LevelInfo, connectionId: ConnectionId): LevelInfo {
        val connection = connection(oldLevel, connectionId)!!
        val connectionComponent = connection[levelConnection]!!

        storeEntities(oldLevel)

        val worldInfo = worldInfo()
        val (nextLevel, connectionId) = changeLevel(connectionComponent, worldInfo)
        return initLevelEntities(nextLevel, connectionId, worldInfo)
    }


    private fun initLevelEntities(newLevel: LevelInfo, connectionId: ConnectionId?, worldInfo: WorldInfo): LevelInfo {
        val entities = allEntities().toList()
        var levelEntity = entityWith(entities, LevelComponent::class)

        if (levelEntity == null) levelEntity = newLevel(newLevel, engine)!!

        var playerEntity: Entity? = null
        var interactionEntity = entityWith(entities, PlayerInteractionComponent::class)

        if (interactionEntity != null) playerEntity = interactionEntity[playerInteraction]?.controlledEntity
        if (playerEntity == null) playerEntity = newPlayer(engine)!!
        val startPosition = playerStartPosition(newLevel, connectionId)
        resetEntity(playerEntity, startPosition)

        if (interactionEntity == null) interactionEntity = newPlayerInteraction(playerEntity, engine)

        levelEntity[level]?.level = newLevel

        //setVisited(connectionId, level)
        setVisited(connectionId, newLevel)

        //send(LevelUnloaded(it))   todo
        val newLevelDescription = levelDescription(newLevel.descriptionId, worldInfo)
        send(LevelLoaded(newLevel, newLevelDescription.params))

        return newLevel
    }

    private fun resetEntity(entity: Entity, startPosition: GridPoint2) {
        entity[position]!!.position = floatPoint(startPosition)
        entity[move]?.path?.reset()
        entity[animation]?.stopAnimation("walk") //todo stop all
    }

    //todo case with multiple enter connections - go first, go back, go second - no storedConnection for already visited level
    //todo for the circular path - go down using one path, go up using another enter
    private fun changeLevel(connection: LevelConnectionComponent?, worldInfo: WorldInfo): Pair<LevelInfo, ConnectionId?> {
        val connectionId = connection?.id
        val connectionType = connection?.type ?: LevelConnectionType.Down
        var backConnectionId: ConnectionId? = null

        val newLevelDescriptionId = connection?.levelDescriptionId ?: worldInfo.entryPoint
        val newLevelDescription = levelDescription(newLevelDescriptionId, worldInfo)

        val storedLevel = LevelRegistry.levelInfoByDesciption(newLevelDescriptionId)?.also {
            println("load cached level: ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            restoreEntities(it)
        }
        val newLevel = storedLevel ?: newLevelInfo(newLevelDescription).also {
            println("create new level ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            newLevelDescription.params.postProcessors.forEach {
                processor ->
                processor.process(it, engine, worldInfo, newLevelDescription)
            }
        }

        LevelRegistry.addLevel(newLevel)

        if (connectionId != null) {
            val backConnectionType = connectionType.opposite()
            //todo take existing
            backConnectionId = randomConnection(newLevel, backConnectionType)
            println("back transition: $backConnectionId")
        }

        return newLevel to backConnectionId
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

    //todo not exactly random - some connections may already be initialized if we were here earlier using another way
    private fun randomConnection(level: LevelInfo, type: LevelConnectionType): ConnectionId {
        val connections = entitiesWith(level.objects, LevelConnectionComponent::class)
            .map { it[levelConnection]!! }
        return connections.filter { it.type == type && !it.visited }.random().id
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