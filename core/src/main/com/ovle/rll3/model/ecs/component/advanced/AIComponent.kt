package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.model.ecs.system.ai.components.EntityBlackboard

enum class AIType {
    Follow,
    RunAway
}

class AIComponent(
    val type: AIType
) : Component {
    lateinit var behaviorTree: BehaviorTree<EntityBlackboard>
}