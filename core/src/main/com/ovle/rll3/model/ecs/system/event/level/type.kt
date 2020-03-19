package com.ovle.rll3.model.ecs.system.event.level

import com.badlogic.ashley.core.Component
import java.util.*

typealias PlayerId = UUID
typealias WorldId = UUID
typealias LevelId = UUID
typealias LevelDescriptionId = String
typealias ConnectionId = UUID

data class StoredEntity(
    val components: Collection<Component>
)