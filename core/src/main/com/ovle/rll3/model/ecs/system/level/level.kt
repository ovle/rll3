package com.ovle.rll3.model.ecs.system.level

import com.badlogic.ashley.core.Engine
import com.ovle.rll3.model.ecs.component.LevelInfo
import com.ovle.rll3.model.procedural.grid.DungeonGenerationSettings
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.procedural.grid.processor.DoorProcessor
import com.ovle.rll3.model.procedural.grid.processor.LightSourceProcessor
import com.ovle.rll3.model.procedural.grid.processor.RoomStructureProcessor
import com.ovle.rll3.model.procedural.grid.processor.RoomsInfoProcessor
import com.ovle.rll3.model.util.gridToTileArray


fun levelInfo(size: Int, gridFactory: GridFactory, generationSettings: DungeonGenerationSettings, engine: Engine): LevelInfo {
    val grid = gridFactory.get(size, generationSettings)
    val tiles = gridToTileArray(grid)

    val result = LevelInfo(tiles)

    //todo inject list by interface
    RoomsInfoProcessor().process(result, engine)
    RoomStructureProcessor().process(result, engine)

    DoorProcessor().process(result, engine)
    LightSourceProcessor().process(result, engine)
//                TrapProcessor().process(result, engine)

    return result
}