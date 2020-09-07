package com.ovle.rll3.model.lightning.other

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.light.AOEData
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.point
import ktx.math.vec2

class EntitiesData(val entities: Array<Entity>, val note: String = "")

class TileTemplate(val size: Int, val data: Array<Int>, val note: String = "")

data class ExpectedResult(
    val areaSize: Int,
    val croppedAreaSize: Int,
    val obstaclesCount: Int
)

fun lightSource(x: Int, y: Int, radius: Int): Entity {
    return Entity().apply {
        add(PositionComponent(point(x, y)))
//        add(AOEData(
//            radius = radius,
//            aoePositions = listOf()
//        ))
    }
}