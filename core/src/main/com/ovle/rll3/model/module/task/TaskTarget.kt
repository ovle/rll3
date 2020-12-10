package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import ktx.ashley.get

//todo remove?
sealed class TaskTarget {
    class EntityTarget(val entity: Entity): TaskTarget() {
        override fun unbox() = entity
        override fun position() = entity[position]!!.gridPosition
    }
    class PositionTarget(val position: GridPoint2): TaskTarget() {
        override fun unbox() = position
        override fun position() = position
    }
    class AreaTarget(val area: Collection<GridPoint2>): TaskTarget() {
        override fun unbox() = area
        override fun position() = throw UnsupportedOperationException("area target not support positions")  //todo lsp violation
    }

    abstract fun unbox(): Any?
    abstract fun position(): GridPoint2
}