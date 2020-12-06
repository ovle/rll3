package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.isAdjacent
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.component.ComponentMappers.move
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.resource
import com.ovle.rll3.model.module.core.component.ComponentMappers.source
import ktx.ashley.get
import ktx.ashley.has


object EntityConditions {

    fun isNearPosition(e: Entity, p: GridPoint2) = e[position]!!.gridPosition.isAdjacent(p)

    fun isAtPosition(e: Entity, p: GridPoint2) = e[position]!!.gridPosition == p

    fun isMoving(e: Entity) = e[move]!!.path.started

    fun isDead(e: Entity) = e[health]!!.isDead

    fun isExists(e: Entity) = e.components.size() > 0   //todo

    fun isLivingEntity(e: Entity) = e.has(health) && !e[health]!!.isDead

    fun isSourceEntity(e: Entity) = e.has(source)

    fun isResourceEntity(e: Entity) = e.has(resource)
}