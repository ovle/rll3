package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.entity.anyTaskPerformer
import java.io.Serializable

//todo progress
data class TaskInfo(
    val template: TaskTemplate,
    var performer: Entity?,   //todo multiple ?
    val target: TaskTarget,
    var started: Boolean = false
): Serializable

