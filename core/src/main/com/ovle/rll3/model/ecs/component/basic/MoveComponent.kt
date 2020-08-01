package com.ovle.rll3.model.ecs.component.basic

import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.component.dto.MovePath

class MoveComponent(val ticksPerTile: Int = 25, val path: MovePath = MovePath()) : BaseComponent
