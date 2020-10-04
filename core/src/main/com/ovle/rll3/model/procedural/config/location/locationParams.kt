package com.ovle.rll3.model.procedural.config.location

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.procedural.config.LocationGenerationParams
import com.ovle.rll3.model.procedural.grid.processor.location.entity.EntityProcessor
import com.ovle.rll3.model.procedural.grid.processor.location.structure.StructureTemplateProcessor
import com.ovle.rll3.model.procedural.grid.world.WorldInfo
import com.ovle.rll3.model.template.TemplatesType
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

fun locationParams(world: WorldInfo, locationPoint: GridPoint2) = LocationGenerationParams(
    templateName = "Common",
    postProcessors = arrayOf(
        StructureTemplateProcessor(structureTemplates(TemplatesType.Common)),
        EntityProcessor(entityTemplates())
    ),
    tileMapper = ::dungeonTileMapper
)