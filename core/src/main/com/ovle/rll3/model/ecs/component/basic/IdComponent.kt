package com.ovle.rll3.model.ecs.component.basic

import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.system.level.EntityId

class IdComponent(
    var id: EntityId
) : BaseComponent