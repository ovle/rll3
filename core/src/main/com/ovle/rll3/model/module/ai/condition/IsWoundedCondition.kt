package com.ovle.rll3.model.module.ai.condition

import com.ovle.rll3.model.module.core.component.Mappers.living
import com.ovle.rll3.model.module.ai.EntityTask
import ktx.ashley.get

class IsWoundedCondition: EntityTask() {

    override fun execute(): Status {
        val livingComponent = currentEntity[living]!!
        val isWounded = livingComponent.health <= (livingComponent.maxHealth / 2)

        return if (isWounded) Status.SUCCEEDED else Status.FAILED
    }
}