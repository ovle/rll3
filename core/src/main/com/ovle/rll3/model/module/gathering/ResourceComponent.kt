package com.ovle.rll3.model.module.gathering

import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.model.module.core.component.BaseComponent

class ResourceComponent(
    val type: ResourceType,
    var amount: ResourceAmount = 0
) : BaseComponent

