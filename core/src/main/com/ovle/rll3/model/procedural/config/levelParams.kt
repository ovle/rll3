package com.ovle.rll3.model.procedural.config

import com.github.czyzby.noise4j.map.generator.room.RoomType.DefaultRoomType.SQUARE
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.*
import com.ovle.rll3.model.procedural.grid.factory.DungeonGridFactory
import com.ovle.rll3.model.procedural.grid.factory.FractalGridFactory
import com.ovle.rll3.model.procedural.grid.processor.EntityProcessor
import com.ovle.rll3.model.procedural.grid.processor.RiverLevelProcessor
import com.ovle.rll3.model.procedural.grid.processor.RiverLevelProcessorParams
import com.ovle.rll3.model.procedural.grid.processor.structure.StructureProcessor
import com.ovle.rll3.model.procedural.grid.processor.structure.StructureProcessorParams
import com.ovle.rll3.model.procedural.grid.processor.structure.StructureTemplateProcessor
import com.ovle.rll3.model.template.entity.entityTemplates
import com.ovle.rll3.model.template.structure.structureTemplates

//val dungeonLevelParams = LevelParams(
//    templateName = "Dungeon",
//    factoryParams = DungeonLevelFactoryParams(
//        size = 25,
//        roomTypes = arrayOf(SQUARE, ROUNDED, CASTLE, DIAMOND),
//        maxRoomSize = 15,
//        minRoomSize = 5,
//        tolerance = 5,
//        windingChance = 0.25f,
//        randomConnectorChance = 0.05f
//    ),
//    gridFactory = DungeonGridFactory(),
//    postProcessors = arrayOf(
//        RoomsInfoProcessor(),
//        RoomStructureProcessor(),
//        EntityProcessor(entityTemplates(Dungeon))
//    ),
//    tileMapper = ::tileMapper
//)
//
//val caveLevelParams = LevelParams(
//    templateName = "Cave",
//    factoryParams = CelullarAutomataLevelFactoryParams(
//        size = 25,
//        connectionStrategy = ConnectionStrategy.ConnectUnconnectedWithPath
//    ),
//    gridFactory = CelullarAutomataGridFactory(),
//    postProcessors = arrayOf(
//        EntityProcessor(entityTemplates(Caves))
//    ),
//    tileMapper = ::tileMapper
//)

val levelParams = LevelParams(
    templateName = "Common",
    factory = FractalGridFactory(
        params = FractalLevelFactoryParams(
            size = 257
        )
    ),
    postProcessors = arrayOf(
        RiverLevelProcessor(
            params = RiverLevelProcessorParams(
                count = 5
            )
        ),
        StructureProcessor(
            params = StructureProcessorParams(
                number = 2,
                size = 20,
                factory = DungeonGridFactory(
                    params = DungeonLevelFactoryParams(
                        size = 20,
                        roomTypes = arrayOf(SQUARE),
                        maxRoomSize = 7,
                        minRoomSize = 3,
                        tolerance = 5,
                        windingChance = 0.25f,
                        randomConnectorChance = 0.05f
                    )
                ),
                tileMapper = ::dungeonTileMapper,
                tilePreFilter = ::groundTileFilter
            )
        ),
        StructureTemplateProcessor(structureTemplates()),
        EntityProcessor(entityTemplates())
    ),
    tileMapper = ::tileMapper
)