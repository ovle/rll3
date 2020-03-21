package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.ecs.system.level.WorldId
import com.ovle.rll3.model.procedural.config.LevelParams
import java.util.*

data class WorldInfo(
    val id: WorldId = UUID.randomUUID(),
    val levels: List<LevelDescription>,
    val entryPoint: LevelDescriptionId
//    val players: Collection<PlayerId> = setOf()
)

data class LevelDescription(
    val id: LevelDescriptionId,
    val params: LevelParams,
    val connections: List<LevelDescriptionId>
) {

}

//data class WorldPlayerInfo(
//    val playerId: PlayerId,
//    val worldId: PlayerId,
//    val currentLevel: LevelDescriptionId
//)

class WorldComponent(var world: WorldInfo): Component