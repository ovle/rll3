package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.task.TaskInfo

enum class VarKey {
    TargetPosition
}

/**
 * @property  task
 * @property  engine
 * @property  vars
 */
data class BaseBlackboard(
    val task: TaskInfo,
    val engine: Engine,
    val vars: MutableMap<VarKey, Any> = mutableMapOf()
)