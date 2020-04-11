package com.ovle.rll3.model.procedural.config

import com.github.czyzby.noise4j.map.generator.room.RoomType.DefaultRoomType.*
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.CelullarAutomataLevelFactoryParams
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.DungeonLevelFactoryParams
import com.ovle.rll3.model.procedural.grid.factory.CelullarAutomataGridFactory
import com.ovle.rll3.model.procedural.grid.factory.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.factory.NoiseGridFactory
import com.ovle.rll3.model.procedural.grid.processor.EntityProcessor
import com.ovle.rll3.model.procedural.grid.processor.RoomStructureProcessor
import com.ovle.rll3.model.procedural.grid.processor.RoomsInfoProcessor
import com.ovle.rll3.model.procedural.grid.processor.StructureProcessor
import com.ovle.rll3.model.procedural.grid.utils.ConnectionStrategy
import com.ovle.rll3.model.template.TemplatesType.*
import com.ovle.rll3.model.template.entity.entityTemplates
import com.ovle.rll3.model.template.structure.structureTemplates
import com.ovle.rll3.model.util.*
import com.ovle.rll3.view.layer.level.caveTileToTexture
import com.ovle.rll3.view.layer.level.dungeonTileToTexture
import com.ovle.rll3.view.layer.level.villageTileToTexture

val dungeonLevelParams = LevelParams(
    factoryParams = DungeonLevelFactoryParams(
        size = 25,
        roomTypes = arrayOf(SQUARE, ROUNDED, CASTLE, DIAMOND),
        maxRoomSize = 15,
        minRoomSize = 5,
        tolerance = 5,
        windingChance = 0.25f,
        randomConnectorChance = 0.05f
    ),
    gridFactory = DungeonGridFactory(),
    postProcessors = arrayOf(
        RoomsInfoProcessor(),
        RoomStructureProcessor(),
        EntityProcessor(entityTemplates(Dungeon))
    ),

    gridValueToTileType = ::dungeonGridValueToTileType,
    tileToTexture = ::dungeonTileToTexture,
    isCellCandidateForConnection = :: isWallCandidate
)

val caveLevelParams = LevelParams(
    factoryParams = CelullarAutomataLevelFactoryParams(
        size = 25,
        connectionStrategy = ConnectionStrategy.ConnectUnconnectedWithPath
    ),
    gridFactory = CelullarAutomataGridFactory(),
    postProcessors = arrayOf(
        EntityProcessor(entityTemplates(Caves))
    ),

    gridValueToTileType = ::caveGridValueToTileType,
    tileToTexture = ::caveTileToTexture,
    isCellCandidateForConnection = :: isWallCandidate
)

val villageLevelParams = LevelParams(
    factoryParams = LevelFactoryParams.NoiseLevelFactoryParams(
        size = 25,
        radius = 2,
        modifier = 1.3f
    ),
    gridFactory = NoiseGridFactory(),
    postProcessors = arrayOf(
        StructureProcessor(structureTemplates(Village)),
        EntityProcessor(entityTemplates(Village))
    ),

    gridValueToTileType = ::villageGridValueToTileType,
    tileToTexture = ::villageTileToTexture,
    isCellCandidateForConnection = :: isCandidate
)

