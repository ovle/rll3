package com.ovle.rll3.model.module.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.system.EventSystem


class AISystem(val behaviorTrees: MutableMap<String, BehaviorTree<BaseBlackboard>>) : EventSystem() {

    override fun subscribe() {
        EventBus.subscribe<Event.AITurnCommand> { onAITurnCommand() }
    }

    private fun onAITurnCommand() {
        EventBus.send(Event.AIFinishedTurnEvent())
    }
}