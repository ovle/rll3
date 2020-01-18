package com.ovle.rll3.model.lightning

import com.badlogic.ashley.core.Entity

class EntitiesData(val entities: Array<Entity>, val note: String = "")

class TileTemplate(val size: Int, val data: Array<Int>, val note: String = "")

data class ExpectedResults(
    val areaSize: Int,
    val croppedAreaSize: Int,
    val obstaclesCount: Int
)