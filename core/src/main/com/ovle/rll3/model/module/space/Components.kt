package com.ovle.rll3.model.module.space

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.gdx.ashley.component.mapper
import com.ovle.rll3.model.util.info
import ktx.ashley.get
import ktx.ashley.has

object Components {
    val position = mapper<PositionComponent>()
    val move = mapper<MoveComponent>()
}


fun Entity.position(): GridPoint2 {
    check(this.has(Components.position)) { "no position for entity ${this.info()}" }
    return this[Components.position]!!.gridPosition
}

fun Entity.positionOrNull(): GridPoint2? {
    return this[Components.position]?.gridPosition
}

fun Entity.setPosition(newPosition: GridPoint2) {
    check(this.has(Components.position)) { "no position for entity ${this.info()}" }
    this[Components.position]!!.gridPosition = newPosition
}