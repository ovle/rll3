package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
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
    /**
     * Waiting for performer to take
     */
    Waiting,

    /**
     * Has performer, behavior tree is in work
     */
    InProgress,

    /**
     * Successful finishing
     */
    Finished,

    /**
     * Can't be finished by given performer
     */
    Failed,

    /**
     * Cancelled by external conditions
     */
    Cancelled
}

