package com.ovle.rll3.model.module.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
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

    private val startFocusEntityId = "elder"
    private val world = initGameInfo.world
    private val locationPoint = initGameInfo.locationPoint


    override fun subscribe() {
        EventBus.subscribe<StartGameCommand> { onStartGameCommand() }
        EventBus.subscribe<ExitGameCommand> { onExitGameCommand() }

        EventBus.subscribe<DestroyEntityCommand> { onDestroyEntityCommand(it.entity) }
        EventBus.subscribe<CreateEntityCommand> { onCreateEntityCommand(it.entityTemplate, it.position) }
    }

    private fun onStartGameCommand() {
        val location = location(locationParams(world, locationPoint), world.random.seed)
        initEntities(location)
    }

    private fun onExitGameCommand() {
        //todo cleanup
        send(GameDidFinishedEvent())
    }

    private fun onCreateEntityCommand(entityTemplate: EntityTemplate, position: GridPoint2) {
        val entity = newTemplatedEntity(randomId(), entityTemplate, engine)
            .apply { this[ComponentMappers.position]!!.gridPosition = position }

        val location = locationInfo()
        location.entities += entity

        send(EntityInitializedEvent(entity))
    }

    private fun onDestroyEntityCommand(entity: Entity) {
        val location = locationInfo()
        location.entities -= entity

        engine.removeEntity(entity)
        send(EntityDestroyedEvent(entity))
    }


    private fun initEntities(location: LocationInfo) {
        val interactionEntity = newPlayerInteraction(engine)
        val locationEntity = newLocation(location, world, engine)!!

        send(LocationLoadedEvent(location, location.params))

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
            heightGrid = heightGrid,
            heatGrid = heatGrid,
            locationPoint = locationPoint
        )

        val postProcessors = generationParams.postProcessors
        postProcessors.forEach {
            processor ->
            processor.process(result, engine)
        }

        return result
    }
}