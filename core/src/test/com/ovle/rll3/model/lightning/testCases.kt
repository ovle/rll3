package com.ovle.rll3.model.lightning

import com.ovle.rll3.model.ecs.component.point
import com.ovle.rll3.model.tile.Tile
import org.junit.jupiter.params.provider.Arguments.of as args
import java.util.stream.Stream.of as streamOf

val fieldOfViewTestCases = streamOf(
    args(tileTemplate1, entities1, ExpectedResults(21, 21, 0)),
    args(tileTemplate1, entities2, ExpectedResults(21, 8, 0)),
    args(tileTemplate2, entities1, ExpectedResults(21, 21, 4)),
    args(tileTemplate2, entities2, ExpectedResults(21, 8, 1))
)

val lineTestCases = streamOf(
    args(
        point(0, 0), point(0, 0),
        arrayOf(point(0, 0))
    ),
    args(
        point(0, 0), point(0, 1),
        arrayOf(point(0, 0), point(0, 1))
    ),
    args(
        point(0, 0), point(1, 1),
        arrayOf(point(0, 0), point(1, 1))
    ),
    args(
        point(0, 0), point(0, 2),
        arrayOf(point(0, 0), point(0, 1), point(0, 2))
    ),
    args(
        point(0, 0), point(2, 0),
        arrayOf(point(0, 0), point(1, 0), point(2, 0))
    ),
    args(
        point(0, 0), point(2, 2),
        arrayOf(point(0, 0), point(1, 1), point(2, 2))
    ),
    args(
        point(0, 0), point(-2, 2),
        arrayOf(point(0, 0), point(-1, 1), point(-2, 2))
    ),
    args(
        point(0, 0), point(2, -2),
        arrayOf(point(0, 0), point(1, -1), point(2, -2))
    ),
    args(
        point(0, 0), point(-2, -2),
        arrayOf(point(0, 0), point(-1, -1), point(-2, -2))
    )
)

//todo
val fovTestCases = streamOf(
    args(
        point(0, 0),
        2,
        5,
        arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0
        ),
        arrayOf(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            1, 1, 0, 0, 0,
            1, 1, 1, 0, 0,
            1, 1, 1, 0, 0
        )
    )
)

val typeArrayToTilesTestCases = streamOf(
    args(
        2,
        arrayOf(
            0, 0,
            1, 0
        ),
        arrayOf(
            Tile(point(0, 0), 1),
            Tile(point(0, 1), 0),
            Tile(point(1, 0), 0),
            Tile(point(1, 1), 0)
        )
    ),
    args(
        2,
        arrayOf(
            0, 0,
            1, 1
        ),
        arrayOf(
            Tile(point(0, 0), 1),
            Tile(point(0, 1), 0),
            Tile(point(1, 0), 1),
            Tile(point(1, 1), 0)
        )
    )
)