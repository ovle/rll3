package com.ovle.rll3.model.module.ai.condition

import com.ovle.rll3.model.module.ai.EntityTask

class IsInDangerCondition: EntityTask() {

    override fun execute(): Status {
//        val livingComponent = currentEntity[living]!!
        val isInDanger = true

        return if (isInDanger) Status.SUCCEEDED else Status.FAILED
    }
}