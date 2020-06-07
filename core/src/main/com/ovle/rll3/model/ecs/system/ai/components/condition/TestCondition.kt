package com.ovle.rll3.model.ecs.system.ai.components.condition

import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.system.ai.components.EntityTask
import ktx.ashley.get

class TestCondition: EntityTask() {

    override fun execute(): Status {
        val livingComponent = currentEntity[living]!!
        val isWounded = livingComponent.health < livingComponent.maxHealth

        return if (isWounded) Status.SUCCEEDED else Status.FAILED
    }
}