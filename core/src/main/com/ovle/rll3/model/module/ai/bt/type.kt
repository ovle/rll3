package com.ovle.rll3.model.module.ai.bt

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.TaskInfo
import com.ovle.rll3.model.module.task.TaskTarget

data class TaskExecParams(
    val btParams: BTParams
)

data class TaskExecResult(
    val status: Task.Status,
    val nextTarget: TaskTarget?
)


data class BTParams(
    val task: TaskInfo,
    val engine: Engine
) {
    val owner: Entity
        get() = task.performer!!
    val entities
        get() = engine.entities.toList()
    val location: LocationInfo
        get() = locationInfo(entities.toTypedArray())!!
}

data class BTInfo(val name: String, val bt: (TaskTargetHolder) -> BehaviorTree<BTParams>)