package com.ovle.rll3.model.module.container

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.component.BaseComponent

//todo
//capacity
//resource filter? i.e. stash can't contain liquids; non-item entities can't be stored (need item concept)
//key? i.e. stash needs a (specific) key to be opened
//states? opened, closed, empty
class ContainerComponent(
    var items: MutableCollection<Entity> = mutableListOf()
) : BaseComponent