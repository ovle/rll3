package com.ovle.rll3.model.module.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.model.module.core.component.BaseComponent

class AIComponent(
    val type: AIType
) : BaseComponent {
    lateinit var behaviorTree: BehaviorTree<EntityBlackboard>
}