package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.space.position


class TaskTarget(val target: Any?) {
    fun asEntity() = target as Entity
    fun asPosition() = target as GridPoint2

    fun position(): GridPoint2 {
        return when (target) {
            is Entity -> target.position()
            is GridPoint2 -> target
            else -> throw IllegalStateException("position not supported by target $target")
        }
    }
}

