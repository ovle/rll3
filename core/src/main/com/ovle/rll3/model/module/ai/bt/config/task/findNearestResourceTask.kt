package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.model.module.core.entity.position
import com.ovle.rll3.model.module.core.entity.resources
import com.ovle.rll3.model.module.gathering.ResourceType

fun findNearestResourceTask(type: ResourceType? = null): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val position = owner.position()

    val resources = btParams.entities.resources(type)
    val nearestResource = resources.map { it to it.position().dst(position) }
        .minByOrNull { it.second }

    if (nearestResource == null) {
        result(FAILED)
    } else {
        result(SUCCEEDED, nearestResource.first)
    }
}