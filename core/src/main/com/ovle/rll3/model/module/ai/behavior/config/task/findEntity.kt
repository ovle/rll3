package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.FAILED
import com.badlogic.gdx.ai.btree.Task.Status.SUCCEEDED
import com.ovle.rll3.EntityFilter
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rll3.model.module.core.entity.on
import com.ovle.rll3.model.module.core.entity.position

fun findEntityOnPositionTask(targetHolder: TaskTargetHolder, filter: EntityFilter): TaskExec = { (btParams) ->
    val position = targetHolder.target(validated = false).position()

    val entity = btParams.entities
        .on(position)
        .firstOrNull(filter)

    if (entity == null) {
        result(FAILED)
    } else {
        result(SUCCEEDED, entity)
    }
}

fun findNearestEntityTask(filter: EntityFilter): TaskExec = { (btParams) ->
    val owner = btParams.owner
    val position = owner.position()

    val entities = btParams.entities
    val nearestEntity = entities
        .filter(filter)
        .map { it to it.position().dst(position) }
        .minByOrNull { it.second }

    if (nearestEntity == null) {
        result(FAILED)
    } else {
        result(SUCCEEDED, nearestEntity.first)
    }
}