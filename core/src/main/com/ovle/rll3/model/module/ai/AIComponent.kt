package com.ovle.rll3.model.module.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.ovle.rll3.model.module.ai.bt.BTParams
import com.ovle.rll3.model.module.core.component.BaseComponent

class AIComponent: BaseComponent {
    var behaviorTree: BehaviorTree<BTParams>? = null
}