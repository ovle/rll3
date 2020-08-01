package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.point

class PositionComponent(var gridPosition: GridPoint2 = point(0, 0)) : BaseComponent