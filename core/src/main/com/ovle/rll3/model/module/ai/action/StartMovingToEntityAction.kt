package com.ovle.rll3.model.module.ai.action

import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.ai.EntityBlackboard
import com.ovle.rll3.model.module.ai.EntityTask
import com.ovle.rll3.model.module.ai.entityQuery
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

        val targetPositionComponent = targetEntity[ComponentMappers.position] ?: return Status.FAILED
        EventBus.send(Event.GameEvent.EntityEvent.EntityStartMoveCommand(currentEntity, targetPositionComponent.gridPosition))

        return Status.SUCCEEDED
    }
}