package com.ovle.rll3.model.ecs.system.ai.components.condition

import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.system.ai.components.EntityTask
import ktx.ashley.get

class IsInDangerCondition: EntityTask() {

    override fun execute(): Status {
//        val livingComponent = currentEntity[living]!!
        val isInDanger = true

        return if (isInDanger) Status.SUCCEEDED else Status.FAILED
    }
}