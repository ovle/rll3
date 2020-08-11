package com.ovle.rll3.model.module.ai.condition

import com.ovle.rll3.model.module.core.component.ComponentMappers.health
import com.ovle.rll3.model.module.ai.EntityTask
import ktx.ashley.get

class IsWoundedCondition: EntityTask() {

    override fun execute(): Status {
        val livingComponent = currentEntity[health]!!
        val isWounded = livingComponent.health <= (livingComponent.maxHealth / 2)

        return if (isWounded) Status.SUCCEEDED else Status.FAILED
    }
}