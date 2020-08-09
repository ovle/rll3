package com.ovle.rll3.model.ecs.component.basic

import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.model.ecs.component.BaseComponent

class ResourceComponent(
    var amount: ResourceAmount = 0
) : BaseComponent