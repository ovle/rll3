package com.ovle.rll3.model.lightning.other

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.dto.aoe.kt.AOEData
import com.ovle.rll3.model.ecs.component.basic.PositionComponent

class EntitiesData(val entities: Array<Entity>, val note: String = "")

class TileTemplate(val size: Int, val data: Array<Int>, val note: String = "")

data class ExpectedResult(
    val areaSize: Int,
    val croppedAreaSize: Int,
    val obstaclesCount: Int
)

fun lightSource(x: Int, y: Int, radius: Int): Entity {
    return Entity().apply {
        add(PositionComponent(floatPoint(x.toFloat(), y.toFloat())))
        add(AOEData(
            radius = radius,
            aoePositions = listOf()
        ))
    }
}