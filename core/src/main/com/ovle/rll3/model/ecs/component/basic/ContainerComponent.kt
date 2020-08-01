package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.BaseComponent

class ContainerComponent(
    var initialized: Boolean = false,
    var items: MutableCollection<Entity> = mutableListOf()
) : BaseComponent