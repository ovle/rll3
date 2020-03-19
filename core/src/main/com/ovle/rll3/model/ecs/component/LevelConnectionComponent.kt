package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.system.event.level.ConnectionId
import com.ovle.rll3.model.ecs.system.event.level.LevelDescriptionId
import java.util.*

class LevelConnectionComponent(
    val id: ConnectionId = UUID.randomUUID(),
    val type: LevelConnectionType,
    val levelDescriptionId: LevelDescriptionId,

    var backConnectionId: ConnectionId? = null,
    var visited: Boolean = false    //todo reset in some cases ?
) : Component {
    operator fun component1() = id
    operator fun component2() = visited
    operator fun component3() = type

    enum class LevelConnectionType {
        Up,
        Down;

        fun opposite() = when(this) {
            Up -> Down
            Down -> Up
        }
    }
}