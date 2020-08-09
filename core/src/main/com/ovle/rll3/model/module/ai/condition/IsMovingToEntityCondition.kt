package com.ovle.rll3.model.module.ai.condition

import com.ovle.rll3.model.module.ai.EntityTask


class IsMovingToEntityCondition: EntityTask() {

//    @TaskAttribute(required = true)
//    lateinit var entity: String

    override fun execute(): Status {

        return Status.FAILED
    }
}