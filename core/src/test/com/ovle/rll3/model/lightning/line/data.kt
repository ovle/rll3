package com.ovle.rll3.model.lightning.line

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.point

data class TestCase(
    val from: GridPoint2,
    val to: GridPoint2,
    val expectedResult: Array<GridPoint2>
)

val testCases = arrayOf(
    TestCase(
        from = point(0, 0), to = point(0, 0),
        expectedResult = arrayOf(
            point(0, 0)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(0, 1),
        expectedResult = arrayOf(
            point(0, 0),
            point(0, 1)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(1, 1),
        expectedResult = arrayOf(
            point(0, 0),
            point(1, 1)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(0, 2),
        expectedResult = arrayOf(
            point(0, 0),
            point(0, 1),
            point(0, 2)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(2, 0),
        expectedResult = arrayOf(
            point(0, 0),
            point(1, 0),
            point(2, 0)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(2, 2),
        expectedResult = arrayOf(
            point(0, 0),
            point(1, 1),
            point(2, 2)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(-2, 2),
        expectedResult = arrayOf(
            point(0, 0),
            point(-1, 1),
            point(-2, 2)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(2, -2),
        expectedResult = arrayOf(
            point(0, 0),
            point(1, -1),
            point(2, -2)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(-2, -2),
        expectedResult = arrayOf(
            point(0, 0),
            point(-1, -1),
            point(-2, -2)
        )
    ),

    TestCase(
        from = point(0, 0), to = point(1, 2),
        expectedResult = arrayOf(
            point(0, 0),
            point(1, 1),
            point(1, 2)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(-1, 2),
        expectedResult = arrayOf(
            point(0, 0),
            point(-1, 1),
            point(-1, 2)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(1, -2),
        expectedResult = arrayOf(
            point(0, 0),
            point(1, -1),
            point(1, -2)
        )
    ),
    TestCase(
        from = point(0, 0), to = point(-1, -2),
        expectedResult = arrayOf(
            point(0, 0),
            point(-1, -1),
            point(-1, -2)
        )
    )
)