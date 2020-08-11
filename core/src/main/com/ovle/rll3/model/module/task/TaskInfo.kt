package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.entity.anyTaskPerformer
import java.io.Serializable


data class TaskInfo(
    val template: TaskTemplate,
    val performer: Entity,   //todo multiple ?
    val target: TaskTarget,
    var started: Boolean = false
): Serializable
