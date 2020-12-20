package com.ovle.rll3.model.module.ai.bt.config

import com.badlogic.gdx.ai.btree.Task.Status.*
import com.ovle.rll3.TaskExec
import com.ovle.rll3.adjacentHV
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.entity.resources
import com.ovle.rll3.model.module.core.entity.setPosition
import com.ovle.rll3.model.module.skill.SkillTemplate
import com.ovle.rll3.model.module.task.EntityConditions.isAtPosition
import com.ovle.rll3.model.module.task.EntityConditions.isMoving
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.util.pathfinding.aStar.path
import com.ovle.rll3.point
import ktx.ashley.get


fun findPositionNearTarget(): TaskExec = { (btParams, target) ->
    val owner = btParams.owner
    val location = btParams.location
    val from = owner.position()
    target as TaskTarget
    val to = target.position()

    val nearPoints = to.adjacentHV().sortedBy { it.dst(from) }
    val nextTarget = nearPoints.find { path(from, it, location).isNotEmpty() }
    val status = if (nextTarget != null) SUCCEEDED else FAILED

    result(status, nextTarget)
}

fun moveTask(): TaskExec =  { (btParams, target) ->
    val owner = btParams.owner
    target as TaskTarget
    val to = target.position()

    when {
        isAtPosition(owner, to) -> result(SUCCEEDED)
        isMoving(owner) -> result(RUNNING)
        else -> {
            send(EntityStartMoveCommand(owner, to))
            val status = if (isMoving(owner)) RUNNING else FAILED
            result(status)
        }
    }
}

fun useSkill(skill: SkillTemplate): TaskExec = { (btParams, target) ->
    val owner = btParams.owner
    target as TaskTarget

    val isSucceeded = skill.isSuccess.invoke(owner, target, btParams.location)

    if (isSucceeded) {
        result(SUCCEEDED)
    } else {
        val targetEntity = target.target
        send(EntityUseSkillCommand(owner, targetEntity, skill))

        result(RUNNING)
    }
}

fun takeTask(): TaskExec = { (btParams, target) ->
    val owner = btParams.owner
    target as TaskTarget
    val carried = target.asEntity()

    val carrierComponent = owner[carrier]!!
    if (carrierComponent.item == carried) SUCCEEDED

    carrierComponent.item = carried
    //todo disable carried entity's systems?
    send(EntityCarryItemEvent(owner, carried))

    result(SUCCEEDED)
}

fun dropTask(): TaskExec = { (btParams, target) ->
    val owner = btParams.owner
    target as TaskTarget
    val to = target.position()

    val carried = owner[carrier]!!.item!!
    carried.setPosition(to)
    owner[carrier]!!.item = null

    send(EntityDropItemEvent(owner, carried, to))

    result(SUCCEEDED)
}

fun findGatheredResource(): TaskExec = { (btParams, _) ->
    val owner = btParams.owner
    val position = owner.position()

    val resources = btParams.entities.resources()
    val closestResource = resources.map { it to it.position().dst(position) }
        .minByOrNull { it.second }

    if (closestResource == null) {
        result(FAILED)
    } else {
        result(SUCCEEDED, closestResource.first)
    }
}

fun findResourceStorage(): TaskExec = { (btParams, target) ->
    //todo
    result(SUCCEEDED, point(123, 77))
}

fun todo(): TaskExec = { (btParams, target) ->
    result(SUCCEEDED)
}