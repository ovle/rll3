package com.ovle.rll3.model.ecs.component.special

import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.system.level.ConnectionId
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId

class LevelConnectionComponent(
    val id: ConnectionId,
    val type: LevelConnectionType,
    val levelDescriptionId: LevelDescriptionId,

    var backConnectionId: ConnectionId? = null,
    var visited: Boolean = false    //todo reset in some cases ?
) : BaseComponent {
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