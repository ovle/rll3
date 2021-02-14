package com.ovle.rll3.model.procedural.config.location

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.gdx.math.component1
import com.ovle.rlUtil.gdx.math.component2
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rlUtil.noise4j.grid.factory.impl.FractalGridFactory
import com.ovle.rlUtil.noise4j.grid.factory.impl.FractalGridFactoryParams
import com.ovle.rlUtil.noise4j.grid.factory.impl.NopGridFactory
import com.ovle.rlUtil.noise4j.grid.factory.impl.NopGridFactoryParams
import com.ovle.rlUtil.noise4j.grid.factory.size
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

fun playgroundParams() = LocationGenerationParams(
    templateName = "Playground",
    locationPoint = point(0, 0),
    heightMapFactory = NopGridFactory(
        params = NopGridFactoryParams(
        size = 16,
        value = outdoorHighGroundTreshold
        )
    ),
    postProcessors = arrayOf(
        StructureTemplateProcessor(
            structureTemplates(TemplatesType.Common, "playground")
        )
    ),
    tileMapper = ::tileMapper
)

fun locationParams(world: WorldInfo, locationPoint: GridPoint2) = LocationGenerationParams(
    templateName = "Common",
    locationPoint = locationPoint,
    heightMapFactory = FractalGridFactory(
        params = FractalGridFactoryParams(
            size = 129,
            startIteration = 0,
            flexibleNoiseValue = 0.2f,
            initialBorderValues = initialBorderValues(world, locationPoint)
        )
    ),
    postProcessors = arrayOf(
//        StructureProcessor(
//            params = StructureProcessorParams(
//                number = 2,
//                size = 20,
//                factory = DungeonGridFactory(
//                    params = DungeonLevelFactoryParams(
//                        size = 20,
//                        roomTypes = arrayOf(SQUARE),
//                        maxRoomSize = 7,
//                        minRoomSize = 3,
//                        tolerance = 5,
//                        windingChance = 0.25f,
//                        randomConnectorChance = 0.05f
//                    )
//                ),
//                tileMapper = ::dungeonTileMapper,
//                tilePreFilter = ::groundTileFilter
//            )
//        ),
        StructureTemplateProcessor(structureTemplates(TemplatesType.Common, "embark")),
        EntityProcessor(entityTemplates())
    ),
    tileMapper = ::tileMapper
)


fun initialBorderValues(world: WorldInfo, locationPoint: GridPoint2): Array<FloatArray>? {
    val grid = world.heightGrid
    val (x, y) = locationPoint
    val result = arrayOf(
        FloatArray(3) { i -> grid[x - 1, y - 1 + i] },
        FloatArray(3) { i -> grid[x, y - 1 + i] },
        FloatArray(3) { i -> grid[x + 1, y - 1 + i] }
    )
    return result
}