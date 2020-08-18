package com.ovle.rll3.model.module.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.model.module.ai.BaseBlackboard.*
import com.ovle.rll3.model.module.core.component.BaseComponent

class AIComponent: BaseComponent {
    var behaviorTree: BehaviorTree<BaseBlackboard>? = null
}