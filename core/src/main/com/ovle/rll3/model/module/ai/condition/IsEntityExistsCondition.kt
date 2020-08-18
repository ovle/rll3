package com.ovle.rll3.model.module.ai.condition

import com.ovle.rll3.model.module.ai.BaseTask
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.task.TaskTarget
import ktx.ashley.get

class IsEntityExistsCondition: BaseTask() {

    override fun executeIntr(): Status {
        val targetEntity = (target as TaskTarget.EntityTarget).entity
        val isExists = !targetEntity.isScheduledForRemoval

        return if (isExists) Status.SUCCEEDED else Status.FAILED
    }
}