package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.isNear
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import ktx.ashley.get
import ktx.ashley.has

fun isNearEntityCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.EntityTarget
    val targetPosition = t.entity[position]?.gridPosition ?: return false
    return e[position]!!.gridPosition.isNear(targetPosition)
}

fun isNearPositionCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.PositionTarget
    return e[position]!!.gridPosition.isNear(t.position)
}

fun isAtPositionCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.PositionTarget
    return e[position]!!.gridPosition == t.position
}

fun isTargetDeadCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.EntityTarget
    return t.entity[ComponentMappers.health]!!.isDead
}

fun isTargetExistsCondition(e: Entity, t: TaskTarget): Boolean {
    t as TaskTarget.EntityTarget
    return !t.entity.isScheduledForRemoval
}

fun isPositionCondition(t: TaskTarget): Boolean = t is TaskTarget.PositionTarget

fun isEntityCondition(t: TaskTarget): Boolean = t is TaskTarget.EntityTarget

fun isLivingEntityCondition(t: TaskTarget): Boolean =
    t is TaskTarget.EntityTarget &&
    t.entity.has(ComponentMappers.health)

fun isSourceEntityCondition(t: TaskTarget): Boolean =
    t is TaskTarget.EntityTarget &&
    t.entity.has(ComponentMappers.source)

fun isResourceEntityCondition(t: TaskTarget): Boolean =
    t is TaskTarget.EntityTarget &&
    t.entity.has(ComponentMappers.resource)
