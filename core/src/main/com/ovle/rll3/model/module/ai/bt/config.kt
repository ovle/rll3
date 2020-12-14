package com.ovle.rll3.model.module.ai.bt

import com.badlogic.gdx.ai.btree.Task.Status.*
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.adjacentHV
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.entity.setPosition
import com.ovle.rll3.model.module.task.EntityConditions.isAtPosition
import com.ovle.rll3.model.module.task.EntityConditions.isMoving
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.module.task.asEntityTarget
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.util.pathfinding.aStar.path
import ktx.ashley.get


val testTreeInfo = BTInfo(
    name = "test",
    bt = tree(
        seq(
            action { _ -> println("1"); FAILED },
            action { _ -> println("2"); SUCCEEDED }
        )
    )
)

val findMoveTargetNear = action { (btParams, target) ->
    val owner = btParams.owner
    val location = btParams.location
    val from = owner.position()
    target as GridPoint2

    val nearPoints = target.adjacentHV().sortedBy { it.dst(from) }
    val result = nearPoints.find { path(from, it, location).isNotEmpty() }
    //todo how to set target for the next task

    if (result != null) SUCCEEDED else FAILED
}

val moveTask = action { (btParams, target) ->
    val owner = btParams.owner
    target as TaskTarget
    val to = target.position()

    when {
        isAtPosition(owner, to) -> SUCCEEDED
        isMoving(owner) -> RUNNING
        else -> {
            send(EntityStartMoveCommand(owner, to))
            val result = isMoving(owner)
            if (result) RUNNING else FAILED
        }
    }
}

val useSkill = action { (btParams, target) ->
    val skillName: String = "todo"  //todo
    val owner = btParams.owner
    target as TaskTarget

    val skillTemplate = TemplatesRegistry.skillTemplates[skillName]!!
    val isSucceeded = skillTemplate.isSuccess
    if (isSucceeded.invoke(owner, target, btParams.location)) SUCCEEDED

    val targetEntity = target.unbox()
    send(EntityUseSkillCommand(owner, targetEntity, skillTemplate))

    RUNNING
}

val takeTask = action { (btParams, target) ->
    val owner = btParams.owner
    target as TaskTarget
    val carried = target.asEntityTarget().unbox()

    val carrierComponent = owner[carrier]!!
    if (carrierComponent.item == carried) SUCCEEDED

    carrierComponent.item = carried
    //todo disable carried entity's systems?
    send(EntityCarryItemEvent(owner, carried))

    SUCCEEDED
}

val dropTask = action { (btParams, target) ->
    val owner = btParams.owner
    target as TaskTarget
    val to = target.position()

    val carried = owner[carrier]!!.item!!
    carried.setPosition(to)
    owner[carrier]!!.item = null

    send(EntityDropItemEvent(owner, carried, to))

    SUCCEEDED
}