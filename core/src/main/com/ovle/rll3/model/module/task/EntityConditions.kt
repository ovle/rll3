package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.gdx.math.isAdj
import com.ovle.rll3.model.module.ai.Components.ai
import com.ovle.rll3.model.module.core.Components.core
import com.ovle.rll3.model.module.core.entity.consumes
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.entity.resources
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.life.Components.life
import com.ovle.rll3.model.module.resource.Components.resource
import com.ovle.rll3.model.module.resource.Components.source
import com.ovle.rll3.model.module.resource.ResourceType.*
import com.ovle.rll3.model.module.space.Components.move
import com.ovle.rll3.model.module.space.Components.position
import com.ovle.rll3.model.module.task.Components.taskPerformer
import com.ovle.rll3.model.util.path
import ktx.ashley.get
import ktx.ashley.has

//todo modules
object EntityConditions {

    fun isNearPosition(e: Entity, p: GridPoint2) = e.has(position) && e.position().isAdj(p)

    fun isAtPosition(e: Entity, p: GridPoint2) = e.has(position) && e.position() == p

    fun isMoving(e: Entity) = e.has(move) && e[move]!!.path.started

    fun isAIActive(e: Entity) = e.has(ai) && e[ai]!!.active

    fun isDead(e: Entity) = e.has(life) && e[life]!!.isDead

    fun isExists(e: Entity) = e[core]!!.isExists

    fun isLivingEntity(e: Entity) = e.has(life) && e.has(life) && !e[life]!!.isDead

    fun isSourceEntity(e: Entity) = e.has(source)

    fun isResourceEntity(e: Entity) = e.has(resource)

    fun isBuildMaterialEntity(e: Entity) = isResourceEntity(e) && e[resource]!!.type == Stone

    fun isFoodEntity(e: Entity) = isResourceEntity(e) && e[resource]!!.type == Food

    fun isHungry(e: Entity, l: LocationInfo) = e.has(life) && e[life]!!.let { it.hunger > it.maxHunger / 2 }

    fun isHaveAvailableFood(e: Entity, l: LocationInfo) = e.has(life) &&
        l.entities.resources(Food).any {
            e.consumes(it) && path(e.position(), it.position(), l).isNotEmpty()
        }

    fun isInDanger(e: Entity, l: LocationInfo) = e.has(life) && e[life]!!.let { it.health < it.maxHealth / 2 }

    fun isTaskPerformer(e: Entity) = e.has(taskPerformer) && !isDead(e)

    fun isFreeTaskPerformer(e: Entity) = isTaskPerformer(e) && e[taskPerformer]!!.current == null

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