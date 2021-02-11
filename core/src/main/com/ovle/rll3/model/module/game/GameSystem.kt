package com.ovle.rll3.model.module.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.core.component.ComponentMappers.core
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.procedural.config.LocationGenerationParams
import com.ovle.rlUtil.RandomParams
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rlUtil.noise4j.grid.gridToTileArray
import com.ovle.rll3.event.*
import com.ovle.rll3.model.procedural.config.location.locationParams
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.screen.game.InitGameInfo
import ktx.ashley.get
import ktx.ashley.has


class GameSystem : EventSystem() {

    private val startFocusEntityId = "elder"
    private lateinit var world: WorldInfo


    override fun subscribe() {
        subscribe<StartGameCommand> { onStartGameCommand(it.gameInfo) }
        subscribe<ExitGameCommand> { onExitGameCommand() }

        subscribe<DestroyEntityCommand> { onDestroyEntityCommand(it.entity) }
        subscribe<CreateEntityCommand> { onCreateEntityCommand(it.entityTemplate, it.position) }
    }

    private fun onStartGameCommand(gameInfo: InitGameInfo) {
        world = gameInfo.world

        val locationPoint = gameInfo.locationPoint
        val locationParams = locationParams(world, locationPoint)
        val location = location(locationParams, world.random.seed)

        initEntities(location)
    }

    private fun onExitGameCommand() {
        //todo cleanup
        send(GameDidFinishedEvent())
    }

    private fun onCreateEntityCommand(entityTemplate: EntityTemplate, startPosition: GridPoint2) {
        val entity = newTemplatedEntity(randomId(), entityTemplate, engine)
            .apply {
                if (has(position)) {
                    setPosition(startPosition)
                }
            }

        val location = locationInfo()
        location.entities += entity

        send(EntityInitializedEvent(entity))
    }

    //todo soft delete? too much trouble with this approach
    private fun onDestroyEntityCommand(entity: Entity) {
        val location = locationInfo()
        location.entities -= entity

        entity[core]!!.deleted = true
//        engine.removeEntity(entity)

        send(EntityDestroyedEvent(entity))
    }


    private fun initEntities(location: LocationInfo) {
        val interactionEntity = newPlayerInteraction(engine)
        val locationEntity = newGame(location, world, engine)!!

        send(LocationLoadedEvent(location))

        location.entities.forEach {
            send(EntityInitializedEvent(it))
        }

        val startEntity = entityNullable(startFocusEntityId)
        startEntity?.let {
            send(FocusEntityCommand(it))
        }
    }

    private fun location(params: LocationGenerationParams, seed: Long): LocationInfo {
        val random = RandomParams(seed)
        val locationPoint = params.locationPoint

        val heightGrid = params.heightMapFactory.get(random)
        val heatValue = world.heatGrid[locationPoint.x, locationPoint.y]
        val heatGrid = Grid(heatValue, heightGrid.width, heightGrid.height) //todo
        val id = randomId()

        val tiles = gridToTileArray(heightGrid, heatGrid, params.tileMapper)

        val result = LocationInfo(
            id = id,
            random = random,
            tiles = tiles,
//            params = generationParams,
//            heightGrid = heightGrid,
//            heatGrid = heatGrid,
            locationPoint = locationPoint
        )

        val postProcessors = params.postProcessors
        postProcessors.forEach {
            processor ->
            processor.process(result, engine)
        }

        return result
    }
}