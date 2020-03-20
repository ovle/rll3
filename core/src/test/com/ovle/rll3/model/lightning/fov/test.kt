package com.ovle.rll3.model.lightning.fov

import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class Test {

    companion object {
        @JvmStatic
        fun args() = testCases
    }

    @ParameterizedTest
    @MethodSource("args")
    fun `test fieldOfView`(testCase: TestCase) {
        val (source, radius, areaSize, tileTemplate, expectedResult) = testCase
        val inFovPointId = 1
        val tiles = TileArray(tileTemplate.map { Tile(it) }.toTypedArray(), areaSize)
        val expectedTiles = TileArray(expectedResult.map { Tile(it) }.toTypedArray(), areaSize)

        val expectedPositions = expectedTiles.indexedTiles()
            .filter { it.second.typeId == inFovPointId }
            .map { it.first }
            .map { expectedTiles.point(it) }.toSet()

        val fov = fieldOfView(
            center = source,
            radius = radius,
            passMapper = ::lightTilePassMapper,
            tiles = tiles,
            obstacles = listOf()
        ).toSet()

        //todo sets deep equality
        Assertions.assertTrue { expectedPositions.containsAll(fov) }
        Assertions.assertTrue { fov.containsAll(expectedPositions) }
    }
}