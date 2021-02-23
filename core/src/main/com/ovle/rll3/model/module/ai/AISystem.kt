package com.ovle.rll3.model.module.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rlUtil.event.EventBus.subscribe
import com.ovle.rll3.event.*
import com.ovle.rll3.model.module.ai.Components.ai
import com.ovle.rll3.model.module.ai.behavior.BTParams
import com.ovle.rll3.model.module.ai.behavior.TaskStatusListener
import com.ovle.rll3.model.module.ai.behavior.TaskTargetHolder
import com.ovle.rll3.model.module.ai.behavior.config.behaviors
import com.ovle.rll3.model.util.aiEntities
import com.ovle.rll3.model.util.locationInfo
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.util.conditions.EntityConditions.isAIActive
import com.ovle.rll3.model.module.task.TaskInfo
import ktx.ashley.get


class AISystem(
    private var isRealTime: Boolean = false
) : EventSystem() {

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        GdxAI.getTimepiece().update(deltaTime);

        if (isRealTime) {
            onTurnChangedEvent(0)
        }
    }

    override fun subscribe() {
        subscribe<BtFinishedEvent> { onBtFinishedEvent(it.tree) }

        subscribe<EntityDiedEvent> { onEntityDiedEvent(it.entity) }
        subscribe<EntityResurrectedEvent> { onEntityResurrectedEvent(it.entity) }

        subscribe<TaskStartedEvent> { onTaskStartedEvent(it.task) }
        subscribe<TaskFinishedEvent> { onTaskFinishedEvent(it.task) }
        if (!isRealTime) {
            subscribe<TurnChangedEvent> { onTurnChangedEvent(it.turn) }
        }
    }

    private fun onEntityDiedEvent(entity: Entity) {
        val aiComponent = entity[ai] ?: return
        aiComponent.active = false

        aiComponent.behaviorTree?.cancel()
    }

    private fun onEntityResurrectedEvent(entity: Entity) {
        val aiComponent = entity[ai] ?: return
        aiComponent.active = true
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

        //todo unconditional rewriting?
        val newBehaviorTree = newBehaviorTree(behaviorTreePrototype, blackboard, taskInfo)
        aiComponent.behaviorTree = newBehaviorTree
    }

    //todo cleanup?
    private fun onTaskFinishedEvent(task: TaskInfo) {
        val aiComponent = task.performer?.get(ai) ?: return

//        aiComponent.behaviorTree!!.cancel()
        aiComponent.behaviorTree = null
    }

    private fun onTurnChangedEvent(turn: Long) {
        val entities = engine.aiEntities()
        processEntities(entities)
    }


    private fun processEntities(entities: List<Entity>) {
        val location = engine.locationInfo()!!
        entities
            .filter { isAIActive(it) }
            .forEach { processEntity(it, location) }
    }

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

    private fun newBehaviorTree(prototype: BehaviorTree<BTParams>, blackboard: BTParams, taskInfo: TaskInfo?) =
        prototype.cloneTask()
            .let { it as BehaviorTree<BTParams> }
            .apply {
                `object` = blackboard
                addListener(TaskStatusListener(this, taskInfo))
            }
}
