package com.ovle.rll3.model.procedural.config

import com.ovle.rll3.model.ecs.component.special.LevelDescription
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId

const val firstLevelDescId: LevelDescriptionId = "1"

//todo will be procedurally generated, so WorldParams will be added

val world = WorldInfo(
    id = randomId(),
    entryPoint = firstLevelDescId,
    levels = listOf(
        LevelDescription(id = firstLevelDescId, params = villageLevelParams, connections = listOf("2")),
        LevelDescription(id = "2", params = dungeonLevelParams, connections = listOf("3.1", "3.2")),
        LevelDescription(id = "3.1", params = caveLevelParams, connections = listOf("4")),
        LevelDescription(id = "3.2", params = dungeonLevelParams, connections = listOf("4")),
        LevelDescription(id = "4", params = caveLevelParams, connections = listOf())
    )
)
