package com.ovle.rll3.model.module.resource

import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.ResourceAmount

class SourceComponent(
    val type: ResourceType,
    val amount: ResourceAmount
) : BaseComponent