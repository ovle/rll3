package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.entity.position
import ktx.ashley.get
import java.lang.IllegalStateException

class TaskTarget(val target: Any?) {
    fun asEntity() = target as Entity
    fun asPosition() = target as GridPoint2

    fun position() = when (target) {
        is Entity -> target.position()
        is GridPoint2 -> target
        else -> throw IllegalStateException("position not supported by target $target")
    }
}