package com.ovle.rll3.model.ecs.system

import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.dto.TimeInfo
import com.ovle.rll3.model.ecs.entity.newLevel
import com.ovle.rll3.model.ecs.entity.newPlayer
import com.ovle.rll3.model.ecs.entity.newPlayerInteraction
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.procedural.config.LevelParams
import com.ovle.rll3.model.procedural.config.levelParams
import com.ovle.rll3.model.util.gridToTileArray


class LevelSystem: EventSystem() {

    private val testSeed = 123L
    private val levelTemplateName = "Village"

    override fun subscribe() {
        EventBus.subscribe<GameStarted> { onGameStartedEvent() }
    }

    private fun onGameStartedEvent(): LevelInfo {
        val level = level(levelParams(levelTemplateName), testSeed)
        return initEntities(level)
    }

    private fun initEntities(level: LevelInfo): LevelInfo {
        val playerEntity = newPlayer(PlayerInfo(randomId()), engine)
        val interactionEntity = newPlayerInteraction(engine)
        val levelEntity = newLevel(level, engine)!!

        send(LevelLoaded(level, level.params))

        level.entities.forEach {
            send(EntityInitialized(it))
        }

        return level
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