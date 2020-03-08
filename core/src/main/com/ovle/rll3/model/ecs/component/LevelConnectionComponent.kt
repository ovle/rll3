package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import java.util.*

class LevelConnectionComponent(
    val id: UUID = UUID.randomUUID(),
    var visited: Boolean = false,
    val type: LevelConnectionType,
    val levelDescriptionId: LevelDescriptionId
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