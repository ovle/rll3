package com.ovle.rll3.model.ecs.component.basic

import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.ResourceGatherCost
import com.ovle.rll3.model.ecs.component.BaseComponent

class SourceComponent(
    val type: ResourceType,
    val amount: ResourceAmount,

    var gatherCostPaid: ResourceGatherCost = 0
) : BaseComponent {

    val isGathered: Boolean
        get() = gatherCostPaid >= type.gatherCost
}

enum class ResourceType(val gatherCost: ResourceGatherCost) {
    Wood(3),
    Stone(8)
}