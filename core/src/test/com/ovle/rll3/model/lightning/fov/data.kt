package com.ovle.rll3.model.lightning.fov

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.point

data class TestCase(
    val source: GridPoint2,
    val radius: Int,
    val areaSize: Int,
    val tileTemplate: Array<Int>,
    val expectedResult: Array<Int>
)

val testCases = arrayOf(
    TestCase(
        source = point(0, 0),
        radius = 2,
        areaSize = 5,
        tileTemplate = arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0
        ),
        expectedResult = arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            1, 1, 0, 0, 0,
            1, 1, 1, 0, 0,
            1, 1, 1, 0, 0
        )
    ),
    TestCase(
        source = point(2, 2),
        radius = 2,
        areaSize = 5,
        tileTemplate = arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0
        ),
        expectedResult = arrayOf(
            0, 1, 1, 1, 0,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            0, 1, 1, 1, 0
        )
    ),
    TestCase(
        source = point(2, 0),
        radius = 2,
        areaSize = 5,
        tileTemplate = arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            1, 1, 1, 1, 1,
            0, 0, 0, 0, 0
        ),
        expectedResult = arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            1, 1, 1, 1, 1
        )
    ),
    TestCase(
        source = point(2, 2),
        radius = 2,
        areaSize = 5,
        tileTemplate = arrayOf(
            0, 0, 0, 0, 0,
            0, 1, 0, 1, 0,
            0, 0, 0, 0, 0,
            0, 1, 0, 1, 0,
            0, 0, 0, 0, 0
        ),
        expectedResult = arrayOf(
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0,
            1, 1, 1, 1, 1,
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0
        )
    ),
    TestCase(
        source = point(2, 2),
        radius = 2,
        areaSize = 5,
        tileTemplate = arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 1, 0, 1, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 0, 0
        ),
        expectedResult = arrayOf(
            0, 1, 0, 1, 0,
            1, 1, 0, 1, 1,
            0, 0, 1, 0, 0,
            1, 1, 0, 1, 1,
            0, 1, 0, 1, 0
        )
    ),
    TestCase(
        source = point(2, 2),
        radius = 2,
        areaSize = 5,
        tileTemplate = arrayOf(
            0, 0, 0, 0, 0,
            0, 1, 1, 1, 0,
            0, 1, 0, 1, 0,
            0, 1, 1, 1, 0,
            0, 0, 0, 0, 0
        ),
        expectedResult = arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0
        )
    )
)