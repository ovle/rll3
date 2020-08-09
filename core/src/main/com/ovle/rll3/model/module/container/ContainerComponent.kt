package com.ovle.rll3.model.module.container

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.BaseComponent

class ContainerComponent(
    var initialized: Boolean = false,
    var items: MutableCollection<Entity> = mutableListOf()
) : BaseComponent