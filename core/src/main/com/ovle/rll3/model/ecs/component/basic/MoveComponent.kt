package com.ovle.rll3.model.ecs.component.basic

import com.ovle.rll3.Ticks
import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.component.dto.MovePath

class MoveComponent(val ticksPerTile: Ticks = 25, val path: MovePath = MovePath()) : BaseComponent
