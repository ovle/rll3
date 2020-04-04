package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component

@StateComponent("light")
class LightSourceComponent(
    val area: AOEData
): Component


