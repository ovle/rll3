package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.adjacentHV
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.util.pathfinding.aStar.path

fun findPositionNearTarget(targetHolder: TaskTargetHolder): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val location = btParams.location
    val from = owner.position()
    val target = targetHolder.target
    target as TaskTarget
    val to = target.position()

    val nearPoints = to.adjacentHV().sortedBy { it.dst(from) }
    val nextTarget = nearPoints.find { path(from, it, location).isNotEmpty() }
    val status = if (nextTarget != null) SUCCEEDED else FAILED

    result(status, nextTarget)
}