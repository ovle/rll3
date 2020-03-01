package com.ovle.rll3.model.ecs.system.level

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.component.LevelInfo
import java.util.*

typealias PlayerId = UUID
typealias WorldId = UUID
typealias LevelId = UUID
typealias LevelDescriptionId = String
typealias ConnectionId = UUID

data class ConnectionData(
    val levelId: LevelId,
    val connectionId: ConnectionId
)

data class LevelTransitionInfo(
    val levelInfo: LevelInfo,
    val connectionId: ConnectionId?,
    val isNew: Boolean
)

data class StoredEntity(
    val components: Collection<Component>
)