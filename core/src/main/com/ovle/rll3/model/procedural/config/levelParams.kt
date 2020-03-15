package com.ovle.rll3.model.procedural.config

import com.github.czyzby.noise4j.map.generator.room.RoomType.DefaultRoomType.*
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.CelullarAutomataLevelFactoryParams
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.DungeonLevelFactoryParams
import com.ovle.rll3.model.procedural.grid.factory.CelullarAutomataGridFactory
import com.ovle.rll3.model.procedural.grid.factory.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.processor.*
import com.ovle.rll3.model.procedural.grid.utils.ConnectionStrategy
import com.ovle.rll3.model.util.caveGridValueToTileType
import com.ovle.rll3.model.util.dungeonGridValueToTileType
import com.ovle.rll3.view.layer.level.caveTileToTexture
import com.ovle.rll3.view.layer.level.dungeonTileToTexture

val dungeonLevelParams = LevelParams(
    factoryParams = DungeonLevelFactoryParams(
        size = 25,
        roomTypes = arrayOf(SQUARE, ROUNDED, CASTLE, DIAMOND),
        maxRoomSize = 15,
        minRoomSize = 5,
        tolerance = 5,
        windingChance = 0.25f,
        randomConnectorChance = 0.05f,
        lightSourceChance = 0.05f,
        doorChance = 0.6f,
        creatureChance = 0.025f,
        trapChance = 0.05f
    ),
    gridFactory = DungeonGridFactory(),
    gridValueToTileType = ::dungeonGridValueToTileType,
    tileToTexture = ::dungeonTileToTexture,
    postProcessors = arrayOf(
        RoomsInfoProcessor(),
        RoomStructureProcessor(),
        DoorProcessor(),
        CreaturesProcessor(),
        LightSourceProcessor()
    )
)

val caveLevelParams = LevelParams(
    factoryParams = CelullarAutomataLevelFactoryParams(
        size = 25,
        connectionStrategy = ConnectionStrategy.ConnectUnconnectedWithPath
    ),
    gridFactory = CelullarAutomataGridFactory(),
    gridValueToTileType = ::caveGridValueToTileType,
    tileToTexture = ::caveTileToTexture,
    postProcessors = arrayOf()
)