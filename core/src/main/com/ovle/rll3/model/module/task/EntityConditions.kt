package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.isAdjacent
import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.core.component.ComponentMappers.move
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.resource
import com.ovle.rll3.model.module.core.component.ComponentMappers.source
import com.ovle.rll3.model.module.core.entity.consumes
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.entity.resources
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.gathering.ResourceType
import com.ovle.rll3.model.module.gathering.ResourceType.*
import com.ovle.rll3.model.util.pathfinding.aStar.path
import ktx.ashley.get
import ktx.ashley.has


object EntityConditions {

    fun isNearPosition(e: Entity, p: GridPoint2) = e.has(position) && e.position().isAdjacent(p)

    fun isAtPosition(e: Entity, p: GridPoint2) = e.has(position) && e.position() == p

    fun isMoving(e: Entity) = e.has(move) && e[move]!!.path.started

    fun isDead(e: Entity) = e.has(health) && e[health]!!.isDead

    fun isExists(e: Entity) = e.components.size() > 0   //todo

    fun isLivingEntity(e: Entity) = e.has(health) && e.has(health) && !e[health]!!.isDead

    fun isSourceEntity(e: Entity) = e.has(source)

    fun isResourceEntity(e: Entity) = e.has(resource)

    fun isHungry(e: Entity, l: LocationInfo) = e.has(health) && e[health]!!.let { it.hunger > it.maxHunger / 2 }

    fun isHaveAvailableFood(e: Entity, l: LocationInfo) = e.has(health) &&
        l.entities.resources(Food).any {
            e.consumes(it) && path(e.position(), it.position(), l).isNotEmpty()
        }

    fun isInDanger(e: Entity, l: LocationInfo) = e.has(health) && e[health]!!.let { it.health > it.maxHealth / 2 }

    fun isHaveAttackTarget(e: Entity, l: LocationInfo): Boolean {
        //todo
        return false
    }

    fun isAttackTarget(e: Entity, l: LocationInfo): Boolean {
        //todo
        return false
    }

    fun isHaveAvailableAttackTarget(e: Entity, l: LocationInfo): Boolean {
        //todo
        return false
    }

}