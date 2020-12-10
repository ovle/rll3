package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.entity.anyTaskPerformer
import java.io.Serializable

/**
 * task for ai
 * no duration (varies depend on conditions)
 * single performer (null if not taken yet)
 */
data class TaskInfo(
    val template: TaskTemplate,
    var performer: Entity?,
    val target: TaskTarget,
    var status: TaskStatus = TaskStatus.Waiting
): Serializable

enum class TaskStatus {
    Waiting,
    InProgress,
    Finished,
    Cancelled
}

