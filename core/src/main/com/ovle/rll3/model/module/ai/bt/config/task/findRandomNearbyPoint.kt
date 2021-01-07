package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.point

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