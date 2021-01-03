package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.component1
import com.ovle.rll3.component2
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.point

fun findRandomNearbyPoint(radius: Int = 5): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val (x, y) = owner.position()
    val resultX = (x-radius..x+radius).random()
    val resultY = (y-radius..y+radius).random()

    result(SUCCEEDED, point(resultX, resultY))
}