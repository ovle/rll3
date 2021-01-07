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

    private val isRealTime = false

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        GdxAI.getTimepiece().update(deltaTime);

        if (isRealTime) {
            onTimeChangedEvent(0)
        }
    }

    override fun subscribe() {
        EventBus.subscribe<BtFinishedEvent> { onBtFinishedEvent(it.tree) }

        EventBus.subscribe<TaskStartedEvent> { onTaskStartedEvent(it.task) }
        EventBus.subscribe<TaskFinishedEvent> { onTaskFinishedEvent(it.task) }
        if (!isRealTime) {
            EventBus.subscribe<TimeChangedEvent> { onTimeChangedEvent(it.turn) }
        }
    }

    private fun onBtFinishedEvent(tree: BehaviorTree<BTParams>) {
        val params = tree.`object` as BTParams
        val owner = params.owner
        val aiComponent = owner[ai] ?: return

        aiComponent.behaviorTree = null
    }

    private fun onTaskStartedEvent(taskInfo: TaskInfo) {
        val performer = taskInfo.performer!!
        val aiComponent = performer[ai] ?: return

        val blackboard = BTParams(performer, taskInfo, engine)
        val taskTemplate = taskInfo.template
        val initialTargetHolder = TaskTargetHolder(taskInfo.target)
        val behaviorTreePrototype = taskTemplate.btTemplate.bt.invoke(initialTargetHolder)

        val newBehaviorTree = newBehaviorTree(behaviorTreePrototype, blackboard, taskInfo)
        aiComponent.behaviorTree = newBehaviorTree
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

    //todo performer filter to allow overwrite auto-behavior with tasks
    private fun processEntity(entity: Entity, location: LocationInfo) {
        behaviorTree(entity, location)?.step()
    }

    private fun behaviorTree(entity: Entity, location: LocationInfo): BehaviorTree<BTParams>? {
        val aiComponent = entity[ai]!!
        val behaviorTree = aiComponent.behaviorTree
        //todo compare with new by priority instead
//        return behaviorTree
        if (behaviorTree != null) return behaviorTree

        return newBehaviorTree(aiComponent, entity, location)
    }

    private fun newBehaviorTree(aiComponent: AIComponent, entity: Entity, location: LocationInfo): BehaviorTree<BTParams> {
        val behaviorName = aiComponent.behavior
        val behavior = behaviors.find { it.name == behaviorName }
        checkNotNull(behavior, { "not found behavior with name $behaviorName" })

        val newBt = behavior.selector.invoke(entity, location)
        val emptyTarget = TaskTargetHolder()

        val behaviorTreePrototype = newBt.bt.invoke(emptyTarget)
        val params = BTParams(entity, null, engine)

        val newBehaviorTree = newBehaviorTree(behaviorTreePrototype, params, null)
        aiComponent.behaviorTree = newBehaviorTree

        return newBehaviorTree
    }

    private fun newBehaviorTree(prototype: BehaviorTree<BTParams>, blackboard: BTParams, taskInfo: TaskInfo?): BehaviorTree<BTParams> {
        return prototype.cloneTask()
            .let { it as BehaviorTree<BTParams> }
            .apply {
                `object` = blackboard
                addListener(TaskStatusListener(this, taskInfo))
            }
    }
}
