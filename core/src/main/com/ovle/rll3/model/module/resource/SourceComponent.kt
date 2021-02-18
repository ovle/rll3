package com.ovle.rll3.model.module.resource

import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.model.module.core.component.BaseComponent

class SourceComponent(
    val type: ResourceType,
    val amount: ResourceAmount
) : BaseComponent