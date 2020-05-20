package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.special.*
import com.ovle.rll3.model.ecs.component.util.Mappers.level
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.move
import com.ovle.rll3.model.ecs.component.util.Mappers.playerInteraction
import com.ovle.rll3.model.ecs.component.util.Mappers.position
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.procedural.grid.processor.LevelConnectionProcessor
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.entity.entityTemplate
import com.ovle.rll3.model.tile.isPassable
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.point
import ktx.ashley.get


class LevelSystem: EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<WorldInitEvent> { loadFirstLevel() }
        EventBus.subscribe<EntityLevelTransition> { loadNextLevel(levelInfo(), it.connectionId as String) }
    }

    private fun loadFirstLevel(): LevelInfo {
        val worldInfo = worldInfo()
        val playerInfo = playerInfo()
        val nextLevel = changeLevel(null, null, worldInfo)
        return initLevelEntities(nextLevel, null, worldInfo, playerInfo)
    }

    private fun loadNextLevel(oldLevel: LevelInfo, connectionId: ConnectionId): LevelInfo {
        val oldConnection = connection(oldLevel, connectionId)!![levelConnection]!!

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

        var playerEntity: Entity? = null
        var interactionEntity = entityWith(entities, PlayerInteractionComponent::class)
        if (interactionEntity != null) playerEntity = interactionEntity[playerInteraction]?.controlledEntity

        val playerTemplate = entityTemplate(TemplatesType.Common, playerInfo.templateName)
        if (playerEntity == null) playerEntity = newTemplatedEntity(randomId(), playerTemplate, engine)

        val startPosition = playerStartPosition(newLevel, oldConnection)
        resetEntity(playerEntity, startPosition)

        if (interactionEntity == null) interactionEntity = newPlayerInteraction(playerEntity, engine)

        levelEntity[level]?.level = newLevel

        //setVisited(connectionId, level)
        setVisited(oldConnection, newLevel)

        val newLevelDescription = levelDescription(newLevel.descriptionId, worldInfo)

        send(LevelLoaded(newLevel, newLevelDescription.params))
        send(EntityInitialized(playerEntity))
        send(EntityChanged(playerEntity))

        newLevel.objects.forEach {
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
        val connectionId = oldConnection?.id
        val newLevelDescriptionId = oldConnection?.levelDescriptionId ?: worldInfo.entryPoint
        val newLevelDescription = levelDescription(newLevelDescriptionId, worldInfo)

        val storedLevel = LevelRegistry.levelInfoByDesciption(newLevelDescriptionId)?.also {
//            println("load cached level: ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            restoreEntities(it)
        }
        val newLevel = storedLevel ?: newLevelInfo(newLevelDescription).also {
//            println("create new level ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
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
//            println("back transition: ${it.backConnectionId}")
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
        val id = randomId()

        return LevelInfo(
            id = id,
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
            var firstStartPosition: GridPoint2? = null
            val playerSpawnPoints = newLevel.structures.flatMap { it.template.playerSpawns }
            if (playerSpawnPoints.isNotEmpty()) {
                firstStartPosition = playerSpawnPoints.random()
            }
            if (firstStartPosition == null) {
                val tiles = newLevel.tiles
                firstStartPosition = tiles.positions().filter { tiles.isPassable(it) }.random()
            }

            return firstStartPosition
        }

        val newConnection = connections.find { it[levelConnection]!!.id == oldConnection.backConnectionId }!!
        val newConnectionPosition = newConnection[position]?.gridPosition!!

        return point(newConnectionPosition).apply { y -= 1 }
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
            level.objects.add(entity)
        }
    }
}