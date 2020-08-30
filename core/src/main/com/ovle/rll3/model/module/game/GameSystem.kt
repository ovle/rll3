package com.ovle.rll3.model.module.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.time.TimeInfo
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.procedural.config.LocationGenerationParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.location.locationParams
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.screen.game.InitGameInfo
import ktx.ashley.get


class GameSystem(initGameInfo: InitGameInfo) : EventSystem() {

    private val startFocusEntityId = "elder1"
    private val world = initGameInfo.world
    private val locationPoint = initGameInfo.locationPoint


    override fun subscribe() {
        EventBus.subscribe<StartGameCommand> { onStartGameCommand() }
        EventBus.subscribe<ExitGameCommand> { onExitGameCommand() }

        EventBus.subscribe<DestroyEntityCommand> { onDestroyEntityCommand(it.entity) }
        EventBus.subscribe<CreateEntityCommand> { onCreateEntityCommand(it.entityTemplate, it.position) }
    }

    private fun onStartGameCommand() {
        val level = location(locationParams(world, locationPoint), world.random.seed)
        initEntities(level)
    }

    private fun onExitGameCommand() {
        //todo cleanup
        send(GameDidFinishedEvent())
    }

    private fun onCreateEntityCommand(entityTemplate: EntityTemplate, position: GridPoint2) {
        val entity = newTemplatedEntity(randomId(), entityTemplate, engine)
            .apply { this[ComponentMappers.position]!!.gridPosition = position }

        send(EntityInitializedEvent(entity))
    }

    private fun onDestroyEntityCommand(entity: Entity) {
        engine.removeEntity(entity)
        send(EntityDestroyedEvent(entity))
    }


    private fun initEntities(location: LocationInfo) {
        val playerEntity = newPlayer(PlayerInfo(randomId()), engine)
        val interactionEntity = newPlayerInteraction(engine)
        val locationEntity = newLocation(location, engine)!!

        send(LevelLoadedEvent(location, location.params))

        location.entities.forEach {
            send(EntityInitializedEvent(it))
        }

        val startEntity = entityNullable(startFocusEntityId)
        startEntity?.let {
            send(FocusEntityCommand(it))
        }
    }

    private fun location(generationParams: LocationGenerationParams, seed: Long): LocationInfo {
        val random = RandomParams(seed)
        val heightGrid = generationParams.heightMapFactory.get(random)
        val heatValue = world.heatGrid[locationPoint.x, locationPoint.y]
        val heatGrid = Grid(heatValue, heightGrid.width, heightGrid.height) //todo
        val id = randomId()

        val tiles = gridToTileArray(heightGrid, heatGrid, generationParams.tileMapper)

        val result = LocationInfo(
            id = id,
            random = random,
            tiles = tiles,
            params = generationParams,
            time = TimeInfo(),
            heightGrid = heightGrid,
            heatGrid = heatGrid
        )

        val postProcessors = generationParams.postProcessors
        postProcessors.forEach {
            processor ->
            processor.process(result, engine)
        }

        return result
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