package com.ovle.rll3.model.ecs.system.ai.components.action

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.system.ai.components.EntityBlackboard
import com.ovle.rll3.model.ecs.system.ai.components.EntityTask
import com.ovle.rll3.model.ecs.system.ai.components.entityQuery
import ktx.ashley.get


@OptIn(ExperimentalStdlibApi::class)
class StartMovingToEntityAction: EntityTask() {

    @TaskAttribute(required = true)
    lateinit var entityQuery: String

    override fun copyTo(otherTask: Task<EntityBlackboard>): Task<EntityBlackboard> {
        return super.copyTo(otherTask).apply {
            otherTask as StartMovingToEntityAction
            otherTask.entityQuery = entityQuery
        }
    }


    override fun execute(): Status {
        println("execute StartMovingToEntityAction")
        val targetEntity = entityQuery(entityQuery, entities) ?: return Status.FAILED

        val targetPositionComponent = targetEntity[Mappers.position] ?: return Status.FAILED
        EventBus.send(Event.EntitySetMoveTarget(currentEntity, targetPositionComponent.gridPosition))

        return Status.SUCCEEDED
    }
}