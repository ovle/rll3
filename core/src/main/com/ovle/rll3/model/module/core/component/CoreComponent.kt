package com.ovle.rll3.model.module.core.component

import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.EntityId

class CoreComponent(
    var id: EntityId,
    var deleted: Boolean = false
) : BaseComponent {
    val isExists
        get() = !deleted
}