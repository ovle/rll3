package com.ovle.rll3.model.module.gathering

import com.ovle.rll3.ResourceGatherCost

enum class ResourceType(val gatherCost: ResourceGatherCost) {
    Wood(3),
    Stone(8),
    Meat(2)
}