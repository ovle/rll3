package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.system.level.EntityId

class IdComponent(
    var id: EntityId
) : Component