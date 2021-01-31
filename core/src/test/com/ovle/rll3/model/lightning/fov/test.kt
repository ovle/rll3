package com.ovle.rll3.model.lightning.fov


class Test {

    companion object {
        @JvmStatic
        fun args() = testCases
    }

//    @ParameterizedTest
//    @MethodSource("args")
//    fun `test fieldOfView`(testCase: TestCase) {
//        val (source, radius, areaSize, tileTemplate, expectedResult) = testCase
//        val inFovPointId = 1
//        val tiles = TileArray(tileTemplate.map { it }.toTypedArray(), areaSize)
//        val expectedTiles = TileArray(expectedResult.map { Tile(it) }.toTypedArray(), areaSize)
//
//        val expectedPositions = expectedTiles.indexedElements()
//            .filter { it.second == inFovPointId }
//            .map { it.first }
//            .map { expectedTiles.point(it) }.toSet()
//
//        val fov = fieldOfView(
//            center = source,
//            radius = radius,
//            passMapper = ::lightTilePassMapper,
//            tiles = tiles,
//            obstacles = listOf()
//        ).toSet()
//
//        //todo sets deep equality
//        Assertions.assertTrue { expectedPositions.containsAll(fov) }
//        Assertions.assertTrue { fov.containsAll(expectedPositions) }
//    }
}