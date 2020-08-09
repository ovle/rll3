package com.ovle.rll3.model.module.space

import com.ovle.rll3.Ticks
import com.ovle.rll3.model.module.core.component.BaseComponent
import com.ovle.rll3.model.module.space.MovePath

class MoveComponent(val ticksPerTile: Ticks = 25, val path: MovePath = MovePath()) : BaseComponent
