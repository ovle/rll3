package com.ovle.rll3.model.module.core.component

import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.component.BaseComponent

class CoreComponent(
    var id: EntityId,
    var deleted: Boolean = false
) : BaseComponent {
    val isExists
        get() = !deleted
}