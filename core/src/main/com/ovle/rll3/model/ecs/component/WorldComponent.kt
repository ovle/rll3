package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.ecs.system.level.PlayerId
import com.ovle.rll3.model.ecs.system.level.WorldId
import com.ovle.rll3.model.procedural.config.LevelSettings
import java.util.*

data class WorldInfo(
    val id: WorldId = UUID.randomUUID(),
    val player: PlayerInfo,
    val levels: Collection<LevelDescription>,
    val entryPoint: LevelDescriptionId
) {

}

data class LevelDescription(
    val id: LevelDescriptionId,
    val settings: LevelSettings,
    val connections: Collection<LevelDescriptionId>
) {

}

data class PlayerInfo(
    val id: PlayerId = UUID.randomUUID(),
    val currentLevel: LevelDescriptionId
) {

}

class WorldComponent(var world: WorldInfo): Component