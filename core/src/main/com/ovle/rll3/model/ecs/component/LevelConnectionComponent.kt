package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import java.util.*

class LevelConnectionComponent(
    val id: UUID = UUID.randomUUID(),
    var visited: Boolean = false,
    val type: LevelConnectionType
) : Component {
    enum class LevelConnectionType {
        Up,
        Down;

        fun opposite() = when(this) {
            Up -> Down
            Down -> Up
        }
    }
}