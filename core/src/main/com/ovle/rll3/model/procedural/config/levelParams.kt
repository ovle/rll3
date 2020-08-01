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

val dungeonLevelParams = LevelParams(
    templateName = "Dungeon",
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
    )
)

val caveLevelParams = LevelParams(
    templateName = "Cave",
    factoryParams = CelullarAutomataLevelFactoryParams(
        size = 25,
        connectionStrategy = ConnectionStrategy.ConnectUnconnectedWithPath
    ),
    gridFactory = CelullarAutomataGridFactory(),
    postProcessors = arrayOf(
        EntityProcessor(entityTemplates(Caves))
    )
)

val villageLevelParams = LevelParams(
    templateName = "Village",
    factoryParams = LevelFactoryParams.NoiseLevelFactoryParams(
        size = 25,
        radius = 2,
        modifier = 1.3f
    ),
    gridFactory = NoiseGridFactory(),
    postProcessors = arrayOf(
        StructureProcessor(structureTemplates(Village)),
        EntityProcessor(entityTemplates(Village))
    )
)

val levelParams = mapOf(
    dungeonLevelParams.templateName to dungeonLevelParams,
    caveLevelParams.templateName to caveLevelParams,
    villageLevelParams.templateName to villageLevelParams
)
