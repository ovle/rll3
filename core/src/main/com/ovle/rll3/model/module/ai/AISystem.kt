package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.ai.bt.BTParams
import com.ovle.rll3.model.module.ai.bt.TaskStatusListener
import com.ovle.rll3.model.module.ai.bt.TaskTargetHolder
import com.ovle.rll3.model.module.ai.bt.config.behavior.behaviors
import com.ovle.rll3.model.module.core.component.ComponentMappers.ai
import com.ovle.rll3.model.module.core.entity.allEntities
import com.ovle.rll3.model.module.core.entity.entitiesWith
import com.ovle.rll3.model.module.core.entity.locationInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.task.TaskInfo
import ktx.ashley.get


class AISystem : EventSystem() {

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
        val blackboard = BTParams(taskInfo, engine)
        val taskTemplate = taskInfo.template
        val initialTargetHolder = TaskTargetHolder(taskInfo.target)
        val behaviorTreePrototype = taskTemplate.btTemplate.bt.invoke(initialTargetHolder)

        startBehaviorTree(behaviorTreePrototype, blackboard, taskInfo, aiComponent)
    }

    //todo cleanup?
    private fun onTaskFinishedEvent(task: TaskInfo) {
        val performer = task.performer!!
        val aiComponent = performer[ai] ?: return

//        aiComponent.behaviorTree!!.cancel()
        aiComponent.behaviorTree = null
    }

    private fun onTimeChangedEvent(turn: Long) {
        val location = locationInfo()
        entitiesWith(allEntities().toList(), AIComponent::class)
            .forEach { processEntity(it, location) }
    }

    private fun processEntity(entity: Entity, location: LocationInfo) {
        behaviorTree(entity, location)?.step()
    }

    private fun behaviorTree(entity: Entity, location: LocationInfo): BehaviorTree<BTParams>? {
        val aiComponent = entity[ai]!!
        val behaviorTree = aiComponent.behaviorTree
        return behaviorTree
        //todo compare with new by priority instead
//        if (behaviorTree != null) return behaviorTree
//        val behaviorName = aiComponent.behavior
//        val behavior = behaviors.find { it.name == behaviorName }
//        checkNotNull(behavior)
//
//        val newBt = behavior.selector.invoke(entity, location)
//        val emptyTarget = TaskTargetHolder()
//
//        val newBehaviorTree = newBt.bt.invoke(emptyTarget)
//        //todo polymorphism for user-started tasks and ai-started tasks
//        val params = BTParams(
//
//        )
//
//        startBehaviorTree(newBehaviorTree, params, null, aiComponent)
//        return newBehaviorTree
    }

    private fun startBehaviorTree(prototype: BehaviorTree<BTParams>, blackboard: BTParams, taskInfo: TaskInfo?, aiComponent: AIComponent) {
        val behaviorTree = prototype.cloneTask()
            .let { it as BehaviorTree<BTParams> }
            .apply { this.`object` = blackboard }

        behaviorTree.addListener(TaskStatusListener(behaviorTree, taskInfo))

        aiComponent.behaviorTree = behaviorTree
    }
}
