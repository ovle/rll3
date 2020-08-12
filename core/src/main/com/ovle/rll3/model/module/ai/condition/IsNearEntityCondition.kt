package com.ovle.rll3.model.module.ai.condition

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.ai.EntityBlackboard
import com.ovle.rll3.model.module.ai.EntityTask
import com.ovle.rll3.model.module.core.entity.entityQuery
import ktx.ashley.get


class IsNearEntityCondition: EntityTask() {

    @TaskAttribute(required = true)
    lateinit var entityQuery: String

    override fun copyTo(otherTask: Task<EntityBlackboard>): Task<EntityBlackboard> {
        return super.copyTo(otherTask).apply {
            otherTask as IsNearEntityCondition
            otherTask.entityQuery = entityQuery
        }
    }

    override fun execute(): Status {
        println("execute IsNearEntityCondition")
        val targetEntity = entityQuery(entityQuery, entities) ?: return Status.FAILED

        val currentPosition = currentEntity[position]!!.gridPosition
        val targetPosition = targetEntity[position]!!.gridPosition
        val isNear = currentPosition.dst2(targetPosition) <= 1

        return if (isNear) Status.SUCCEEDED else Status.FAILED
    }
}