package com.ovle.rll3.model.module.resource

import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.model.module.core.component.EntityComponent

class ResourceComponent(
    val type: ResourceType,
    var amount: ResourceAmount = 0
) : EntityComponent()

