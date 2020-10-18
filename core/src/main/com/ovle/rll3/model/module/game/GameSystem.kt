package com.ovle.rll3.model.module.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.EntityId
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.component.ComponentMappers.game
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.procedural.config.LocationGenerationParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.location.locationParams
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.point
import com.ovle.rll3.screen.game.InitGameInfo
import ktx.ashley.get


class GameSystem(initGameInfo: InitGameInfo) : EventSystem() {

    private val startFocusEntityId: EntityId? = null
    private val startFocusPoint: GridPoint2? = point(3, 3)
    private val partyIds = setOf("b1", "w1")

    private val world = initGameInfo.world
//    private val locationPoint = initGameInfo.locationPoint


    override fun subscribe() {
        EventBus.subscribe<StartGameCommand> { onStartGameCommand() }
        EventBus.subscribe<ExitGameCommand> { onExitGameCommand() }

        EventBus.subscribe<DestroyEntityCommand> { onDestroyEntityCommand(it.entity) }
        EventBus.subscribe<CreateEntityCommand> { onCreateEntityCommand(it.entityTemplate, it.position) }
    }

    private fun onStartGameCommand() {
        val location = location(locationParams(world), world.random.seed)
        initEntities(location)
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
        val interactionEntity = newPlayerInteraction(engine)
        val locationEntity = newLocation(location, world, engine)!!

        locationEntity[game]!!.party.addAll(partyIds)

        send(LocationLoadedEvent(location, location.params))

        location.entities.forEach {
            send(EntityInitializedEvent(it))
        }

        initFocus()
    }

    private fun initFocus() {
        when {
            startFocusEntityId != null -> send(FocusEntityCommand(entity(startFocusEntityId)))
            startFocusPoint != null -> send(FocusPointCommand(startFocusPoint))
        }
    }

    private fun location(generationParams: LocationGenerationParams, seed: Long): LocationInfo {
        val random = RandomParams(seed)
        val id = randomId()

        val gridSize = 7    //todo
        val grid = Grid(gridSize, gridSize)
        val tiles = gridToTileArray(grid, generationParams.tileMapper)

        val result = LocationInfo(
            id = id,
            random = random,
            tiles = tiles,
            params = generationParams
//            locationPoint = locationPoint
        )

        val postProcessors = generationParams.postProcessors
        postProcessors.forEach {
            processor ->
            processor.process(result, engine)
        }

        return result
    }
}