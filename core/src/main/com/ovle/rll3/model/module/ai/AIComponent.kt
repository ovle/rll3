package com.ovle.rll3.model.module.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.model.module.ai.behavior.BTParams
import com.ovle.rll3.model.module.core.component.BaseComponent

class AIComponent(var behavior: String): BaseComponent {
    var behaviorTree: BehaviorTree<BTParams>? = null
    var active: Boolean = true
}