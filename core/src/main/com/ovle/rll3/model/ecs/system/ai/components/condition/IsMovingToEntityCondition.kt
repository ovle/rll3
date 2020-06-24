package com.ovle.rll3.model.ecs.system.ai.components.condition

import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.ovle.rll3.model.ecs.component.util.Mappers.living
import com.ovle.rll3.model.ecs.system.ai.components.EntityTask
import ktx.ashley.get


class IsMovingToEntityCondition: EntityTask() {

//    @TaskAttribute(required = true)
//    lateinit var entity: String

    override fun execute(): Status {

        return Status.FAILED
    }
}