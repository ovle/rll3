package com.ovle.rll3.model.lightning


val tileTemplate1 = TileTemplate(5,
    arrayOf(
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0
    ),
    note = "5 x 5, empty"
)

val tileTemplate2 = TileTemplate(5,
    arrayOf(
        0, 0, 0, 0, 0,
        0, 1, 0, 1, 0,
        0, 0, 0, 0, 0,
        0, 1, 0, 1, 0,
        0, 0, 0, 0, 0
    ),
    note = "5 x 5, pillars"
)

val entities1 = EntitiesData(
    entities = arrayOf(
        lightSource(2, 2, 2)
    ),
    note = "1 light in center of 5 x 5"
)

val entities2 = EntitiesData(
    entities = arrayOf(
        lightSource(0, 0, 2)
    ),
    note = "1 light in corner of 5 x 5"
)