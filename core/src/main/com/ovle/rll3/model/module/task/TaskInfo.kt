package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import java.io.Serializable

data class TaskInfo(
    val template: TaskTemplate,
    var performer: Entity?,   //todo multiple ?
    val target: TaskTarget,
    var started: Boolean = false
): Serializable

