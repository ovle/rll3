package com.ovle.rll3.model.module.resource

import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.model.module.core.component.EntityComponent

class SourceComponent(
    val type: ResourceType,
    val amount: ResourceAmount
) : EntityComponent()