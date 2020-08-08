package com.ovle.rll3.model.ecs.system

import com.ovle.rll3.TileType
import com.ovle.rll3.event.Event.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.dto.LevelDescription
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.component.special.LevelComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.level
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.procedural.config.levelParams
import com.ovle.rll3.model.tile.groundTileId
import com.ovle.rll3.model.tile.wallTileId
import com.ovle.rll3.model.tile.waterTileId
import com.ovle.rll3.model.util.gridToTileArray
import ktx.ashley.get


class LevelSystem: EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<WorldInit> { loadFirstLevel() }
    }

    private fun loadFirstLevel(): LevelInfo {
        val worldInfo = worldInfo()
        val playerInfo = playerInfo()
        val nextLevel = changeLevel(null, worldInfo)
        return initLevelEntities(nextLevel, worldInfo, playerInfo)
    }

    private fun initLevelEntities(newLevel: LevelInfo, worldInfo: WorldInfo, playerInfo: PlayerInfo): LevelInfo {
        val entities = allEntities().toList()
        var levelEntity = entityWith(entities, LevelComponent::class)

        if (levelEntity == null) levelEntity = newLevel(newLevel, engine)!!

        levelEntity[level]?.level = newLevel

        val interactionEntity = newPlayerInteraction(engine)
        val newLevelDescription = newLevel.description

        send(LevelLoaded(newLevel, levelParams(newLevelDescription)))

        newLevel.entities.forEach {
            send(EntityInitialized(it))
        }

        return newLevel
    }

    private fun changeLevel(oldLevel: LevelInfo?, worldInfo: WorldInfo): LevelInfo {
        val newLevelDescriptionId = worldInfo.entryPoint
        val newLevelDescription = levelDescription(newLevelDescriptionId, worldInfo)
        val newLevel = newLevelInfo(newLevelDescription, worldInfo).also {
//            println("create new level ${it.id}, description: ${newLevelDescription.id}, transition: $connectionId")
            val postProcessors = levelParams(newLevelDescription).postProcessors
            postProcessors.forEach {
                processor ->
                processor.process(it, worldInfo, engine)
            }
        }

        return newLevel
    }

    private fun levelParams(levelDescription: LevelDescription) = levelParams.getValue(levelDescription.params)

    /*
fun dungeonGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= DungeonGridFactory.wallTreshold -> wallTileId
        gridValue == DungeonGridFactory.floorTreshold -> groundTileId
        gridValue == DungeonGridFactory.corridorTreshold -> corridorTileId
        else -> throw RuntimeException("illegal tile value: $gridValue")
    }
}

fun caveGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= CelullarAutomataGridFactory.wallMarker -> wallTileId
        gridValue >= CelullarAutomataGridFactory.pitMarker -> pitFloorTileId
        else -> groundTileId //todo
    }
}

fun villageGridValueToTileType(gridValue: Float): TileType {
    return when {
        gridValue >= NoiseGridFactory.wallTreshold -> wallTileId
        gridValue >= NoiseGridFactory.floorTreshold -> groundTileId
        else -> waterTileId //todo
    }
}
    */

    private fun gridValueToTileType(value: Float): TileType {
        return when {
            value >= 0.8 -> wallTileId
            value >= 0.5 -> groundTileId
            else -> waterTileId
        }
    }

    private fun newLevelInfo(levelDescription: LevelDescription, worldInfo: WorldInfo): LevelInfo {
        val levelParams = levelParams(levelDescription)
        val factoryParams = levelParams.factoryParams
        val grid = levelParams.gridFactory.get(factoryParams, worldInfo)
        val tiles = gridToTileArray(grid, ::gridValueToTileType)
        val id = randomId()

        return LevelInfo(
            id = id,
            tiles = tiles,
            description = levelDescription
        )
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