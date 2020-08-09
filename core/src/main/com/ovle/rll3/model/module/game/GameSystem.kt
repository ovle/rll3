package com.ovle.rll3.model.module.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.GameEvent.EntityEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.time.TimeInfo
import com.ovle.rll3.model.module.core.component.Mappers
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.procedural.config.levelParams
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.util.gridToTileArray
import ktx.ashley.get


class GameSystem: EventSystem() {

    private val testSeed = 123L
    private val levelTemplateName = "Village"
    private val startFocusEntityId = "elder1"

    override fun subscribe() {
        EventBus.subscribe<StartGameCommand> { onStartGameCommand() }

        EventBus.subscribe<DestroyEntityCommand> { onDestroyEntityCommand(it.entity) }
        EventBus.subscribe<CreateEntityCommand> { onCreateEntityCommand(it.entityTemplate, it.position) }
    }

    private fun onStartGameCommand() {
        val level = level(levelParams(levelTemplateName), testSeed)
        initEntities(level)
    }

    private fun initEntities(level: LevelInfo) {
        val playerEntity = newPlayer(PlayerInfo(randomId()), engine)
        val interactionEntity = newPlayerInteraction(engine)
        val levelEntity = newLevel(level, engine)!!

        send(LevelLoadedEvent(level, level.params))

        level.entities.forEach {
            send(EntityInitializedEvent(it))
        }

        val startEntity = entity(startFocusEntityId)
        send(FocusEntityCommand(startEntity))
    }

    private fun level(levelParams: LevelParams, seed: Long): LevelInfo {
        val factoryParams = levelParams.factoryParams
        val grid = levelParams.gridFactory.get(factoryParams, seed)
        val tiles = gridToTileArray(grid, levelParams.tileMapper)
        val id = randomId()

        val result = LevelInfo(
            id = id,
            seed = seed,
            tiles = tiles,
            params = levelParams,
            time = TimeInfo()
        )

        val postProcessors = levelParams(levelParams.templateName).postProcessors
        postProcessors.forEach {
            processor ->
            processor.process(result, engine)
        }

        return result
    }

    private fun levelParams(name: String) = levelParams.getValue(name)

    private fun onCreateEntityCommand(entityTemplate: EntityTemplate, position: GridPoint2) {
        val entity = newTemplatedEntity(randomId(), entityTemplate, engine)
            .apply { this[Mappers.position]!!.gridPosition = position }

        send(EntityInitializedEvent(entity))
    }

    private fun onDestroyEntityCommand(entity: Entity) {
        engine.removeEntity(entity)
        send(EntityDestroyedEvent(entity))
    }

//    @OptIn(ExperimentalStdlibApi::class)
//    private fun playerStartPosition(newLevel: LevelInfo, oldConnection: LevelConnectionComponent?, r: Random): GridPoint2 {
//        val connections = entitiesWith(newLevel.entities, LevelConnectionComponent::class)
//        check(connections.isNotEmpty())
//
//        val tiles = newLevel.tiles
//        if (oldConnection == null) {
//            var firstStartPosition: GridPoint2? = null
//            val playerSpawnPoints = newLevel.structures.flatMap { it.template.playerSpawns }
//            if (playerSpawnPoints.isNotEmpty()) {
//                firstStartPosition = playerSpawnPoints.randomOrNull(r)?.apply { y = tiles.size - y - 1 }
//            }
//            if (firstStartPosition == null) {
//                firstStartPosition = tiles.positions().filter { tiles.isPassable(it) }.random(r)
//            }
//
//            return firstStartPosition
//        }
//
//        val newConnection = connections.find { it[levelConnection]!!.id == oldConnection.backConnectionId }!!
//        val newConnectionPosition = newConnection[position]?.gridPosition!!
//        val entityPositions = newLevel.entities.positions()
//        val nearPoints = newConnectionPosition.nearExclusive()
//            .filter { tiles.isPointValid(it.x, it.y) && tiles.isPassable(it) && it !in entityPositions } //todo
//        check(nearPoints.isNotEmpty())
//
//        return nearPoints.random(r)
//    }
}