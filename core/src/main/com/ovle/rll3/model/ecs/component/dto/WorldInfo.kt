package com.ovle.rll3.model.ecs.component.dto

import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.ecs.system.level.WorldId
import java.io.Serializable

data class WorldInfo(
    val id: WorldId,
    val seed: Long,
    val levels: List<LevelDescription>,
    val entryPoint: LevelDescriptionId
): Serializable