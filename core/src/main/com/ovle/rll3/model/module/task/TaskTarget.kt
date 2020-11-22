package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2

//todo remove?
sealed class TaskTarget {
    class EntityTarget(val entity: Entity): TaskTarget() {
        override fun unbox() = entity
    }
    class PositionTarget(val position: GridPoint2): TaskTarget() {
        override fun unbox() = position
    }
    class AreaTarget(val area: Collection<GridPoint2>): TaskTarget() {
        override fun unbox() = area
    }

    abstract fun unbox(): Any?
}