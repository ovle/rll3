package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.*
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.task.TaskTarget
import com.ovle.rll3.model.util.pathfinding.aStar.path

fun findPositionNearTarget(targetHolder: TaskTargetHolder): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val location = btParams.location
    val from = owner.position()
    val target = targetHolder.target()
    val to = target.position()

    val nearPoints = to.adjacentHV().sortedBy { it.dst(from) }
    val nextTarget = nearPoints.find { path(from, it, location).isNotEmpty() }
    val status = if (nextTarget != null) SUCCEEDED else FAILED

    result(status, nextTarget)
}

fun findRandomNearbyPoint(radius: Int = 3): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val location = btParams.location
    val (x, y) = owner.position()
    val xRange = (x-radius..x+radius)
    val yRange = (y-radius..y+radius)
    val points = xRange.zip(yRange).map { point(it.first, it.second) }
        .filter { location.tiles.isPointValid(it) }

    if (points.isEmpty()) {
        result(FAILED)
    } else {
        result(SUCCEEDED, points.random())
    }
}