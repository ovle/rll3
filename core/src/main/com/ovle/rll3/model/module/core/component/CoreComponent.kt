package com.ovle.rll3.model.module.core.component

import com.ovle.rll3.EntityId

class CoreComponent(
    var id: EntityId,
    var deleted: Boolean = false
) : EntityComponent() {
    val isExists
        get() = !deleted
}