package com.ovle.rll3.model.module.resource

import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.ResourceAmount

class ResourceComponent(
    val type: ResourceType,
    var amount: ResourceAmount = 0
) : BaseComponent

