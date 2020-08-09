package com.ovle.rll3.model.module.light

import com.badlogic.gdx.math.GridPoint2

data class AOEData(
    val radius: Int,
    val aoePositions: List<AOETilePosition> = listOf()
)

data class AOETilePosition(
    val value: Float,
    val tilePosition: GridPoint2
)
