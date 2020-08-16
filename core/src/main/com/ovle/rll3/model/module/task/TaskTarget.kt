package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2

sealed class TaskTarget {
    class EntityTarget(val entity: Entity): TaskTarget()
    class PositionTarget(val position: GridPoint2): TaskTarget()
    class AreaTarget(val area: Collection<GridPoint2>): TaskTarget()
}