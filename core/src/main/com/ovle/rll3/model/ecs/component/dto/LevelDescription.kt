package com.ovle.rll3.model.ecs.component.dto

import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.ecs.system.level.LevelParamsId
import java.io.Serializable

data class LevelDescription(
    val id: LevelDescriptionId,
    val params: LevelParamsId,
    val connections: List<LevelDescriptionId>
): Serializable