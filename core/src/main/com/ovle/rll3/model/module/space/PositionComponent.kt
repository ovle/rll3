package com.ovle.rll3.model.module.space

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.gdx.math.point
import com.ovle.rll3.model.module.core.component.EntityComponent

class PositionComponent(var gridPosition: GridPoint2 = point(0, 0)) : EntityComponent()