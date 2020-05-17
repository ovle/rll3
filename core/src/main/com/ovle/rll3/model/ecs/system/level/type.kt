package com.ovle.rll3.model.ecs.system.level

import com.badlogic.ashley.core.Component
import java.util.*

typealias EntityId = String
typealias PlayerId = String
typealias WorldId = String
typealias LevelId = String
typealias LevelDescriptionId = String
typealias ConnectionId = String

data class StoredEntity(
    val id: String,
    val components: Collection<Component>
)