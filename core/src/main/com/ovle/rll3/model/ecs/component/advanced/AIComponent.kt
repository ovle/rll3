package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.system.ai.components.AIType
import com.ovle.rll3.model.ecs.system.ai.components.EntityBlackboard

class AIComponent(
    val type: AIType
) : BaseComponent {
    lateinit var behaviorTree: BehaviorTree<EntityBlackboard>
}