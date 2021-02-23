package com.ovle.rll3.model.module.space

import com.ovle.rlUtil.gdx.ashley.component.mapper

object Components {
    val position = mapper<PositionComponent>()
    val move = mapper<MoveComponent>()
}