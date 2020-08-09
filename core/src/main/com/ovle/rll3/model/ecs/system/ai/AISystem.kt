package com.ovle.rll3.model.ecs.system.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.GameEvent.EntityEvent.*
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.ecs.component.advanced.AIComponent
import com.ovle.rll3.model.ecs.component.util.Mappers.ai
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.ecs.entity.entitiesWith
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.ai.components.EntityBlackboard
import ktx.ashley.get


class AISystem(val behaviorTrees: MutableMap<String, BehaviorTree<EntityBlackboard>>) : EventSystem() {

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        GdxAI.getTimepiece().update(deltaTime);
    }

    override fun subscribe() {
        EventBus.subscribe<EntityInitializedEvent> { onEntityInitialized(it.entity) }
        EventBus.subscribe<TimeChangedEvent> { onTimeChanged(it.turn) }
    }

    private fun onEntityInitialized(entity: Entity) {
        val aiComponent = entity[ai] ?: return
        val type = aiComponent.type
        val typeName = type.name.decapitalize()

        val blackboard = EntityBlackboard(entity, engine)

        val behaviorTreePrototype = behaviorTrees[typeName]!!
        val behaviorTree = behaviorTreePrototype.cloneTask()
            .let { it as BehaviorTree<EntityBlackboard> }
            .apply { this.`object` = blackboard }

        aiComponent.behaviorTree = behaviorTree
    }

    private fun onTimeChanged(turn: Long) {
        entitiesWith(allEntities().toList(), AIComponent::class)
            .forEach { processEntity(it) }
    }


    private fun processEntity(entity: Entity) {
        val aiComponent = entity[ai]!!
        val behaviorTree = aiComponent.behaviorTree

        behaviorTree.step()
    }
}