package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

class ContainerComponent(
    var initialized: Boolean = false,
    var items: MutableCollection<Entity> = mutableListOf()
) : Component