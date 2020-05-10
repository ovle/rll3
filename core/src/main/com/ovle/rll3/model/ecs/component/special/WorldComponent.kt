package com.ovle.rll3.model.ecs.component.special

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.ecs.system.level.WorldId
import com.ovle.rll3.model.procedural.config.LevelParams
import java.util.*

data class WorldInfo(
    val id: WorldId = UUID.randomUUID(),
    val levels: List<LevelDescription>,
    val entryPoint: LevelDescriptionId
)

class TimeInfo {
    var turn: Long = 0
    var fractionTicks: Int = 0
}

data class LevelDescription(
    val id: LevelDescriptionId,
    val params: LevelParams,
    val connections: List<LevelDescriptionId>
)

class WorldComponent(
    var world: WorldInfo,
    var time: TimeInfo
): Component