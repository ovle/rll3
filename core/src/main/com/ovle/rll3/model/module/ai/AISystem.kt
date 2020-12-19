package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.Task
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.ai.bt.BTParams
import com.ovle.rll3.model.module.core.component.ComponentMappers.ai
import com.ovle.rll3.model.module.core.entity.allEntities
import com.ovle.rll3.model.module.core.entity.entitiesWith
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.task.TaskInfo
import ktx.ashley.get


class AISystem() : EventSystem() {
//class AISystem(val behaviorTrees: MutableMap<String, BehaviorTree<BaseBlackboard>>) : EventSystem() {

    private val isRealTime = true

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        GdxAI.getTimepiece().update(deltaTime);

        if (isRealTime) {
            onTimeChangedEvent(0)
        }
    }

    override fun subscribe() {
        EventBus.subscribe<TaskStartedEvent> { onTaskStartedEvent(it.task) }
        EventBus.subscribe<TaskFinishedEvent> { onTaskFinishedEvent(it.task) }
        if (!isRealTime) {
            EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it.turn) }
        }
    }

    private fun onTaskStartedEvent(taskInfo: TaskInfo) {
        val performer = taskInfo.performer!!
        val aiComponent = performer[ai] ?: return
        val blackboard = BTParams(taskInfo, engine, null)
        val taskTemplate = taskInfo.template
        val behaviorTreePrototype = taskTemplate.btInfo.bt

        val behaviorTree = behaviorTreePrototype.cloneTask()
            .let { it as BehaviorTree<BTParams> }
            .apply { this.`object` = blackboard }

        behaviorTree.addListener(
            object : TaskStatusListener() {
                override fun statusUpdated(task: Task<BTParams>, previousStatus: Task.Status?) {
                    val root = behaviorTree.getChild(0)
                    if (task == root) {
                        val status = task.status
//                        println("statusUpdated: $status;  performer: ${task.`object`.task.performer};  target: ${task.`object`.task.target}")
                        if (status == Task.Status.SUCCEEDED) {
                            //todo cleanup?
                            send(TaskSucceedCommand(taskInfo))
                        }
                    }
                }
            }
        )

        aiComponent.behaviorTree = behaviorTree
    }

    //todo cleanup?
    private fun onTaskFinishedEvent(task: TaskInfo) {
        val performer = task.performer!!
        val aiComponent = performer[ai] ?: return

//        aiComponent.behaviorTree!!.cancel()
        aiComponent.behaviorTree = null
    }

    private fun onTimeChangedEvent(turn: Long) {
        entitiesWith(allEntities().toList(), AIComponent::class)
            .forEach { processEntity(it) }
    }

    private fun processEntity(entity: Entity) {
        val aiComponent = entity[ai]!!
        val behaviorTree = aiComponent.behaviorTree ?: return

        behaviorTree.step()
    }
}

abstract class TaskStatusListener: BehaviorTree.Listener<BTParams> {
    override fun childAdded(task: Task<BTParams>?, index: Int) {}
}