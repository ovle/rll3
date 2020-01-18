package com.ovle.rll3.model.lightning

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.entityWith
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.discretization.bresenham.filledCircle
import com.ovle.rll3.model.util.discretization.bresenham.line
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.cropArea
import com.ovle.rll3.model.util.lineOfSight.rayTracing.fieldOfView
import com.ovle.rll3.model.util.lineOfSight.rayTracing.obstacles
import com.ovle.rll3.model.util.typeArrayToTiles
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class LevelTest {

    companion object {
        @JvmStatic
        fun lightningTestArgs()  = fieldOfViewTestCases
        @JvmStatic
        fun lineTestArgs()  = lineTestCases
        @JvmStatic
        fun fovTestArgs()  = fovTestCases
        @JvmStatic
        fun typeArrayToTilesTestArgs()  = typeArrayToTilesTestCases
    }

    @ParameterizedTest
    @MethodSource("lightningTestArgs")
    fun `test lightning`(tileTemplate: TileTemplate, entitiesData: EntitiesData, expectedResults: ExpectedResults) {
        val entities = entitiesData.entities.toList()
        val lightSourceEntity = entityWith(entities, LightComponent::class)
        val light = lightSourceEntity.component(LightComponent::class)!!
        val lightPosition = lightSourceEntity.component(PositionComponent::class)!!

        val area = filledCircle(lightPosition.position, light.radius)
        assertEquals(expectedResults.areaSize, area.size, "area size")

        val size = tileTemplate.size
        val tiles = TileArray(typeArrayToTiles(tileTemplate.data, size), size)

        val croppedArea = cropArea(area, tiles)
        assertEquals(expectedResults.croppedAreaSize, croppedArea.size, "cropped area size")

        val obstacles = obstacles(croppedArea, ::lightTilePassMapper, tiles)
        assertEquals(expectedResults.obstaclesCount, obstacles.size, "obstacles count")
    }

    @ParameterizedTest
    @MethodSource("lineTestArgs")
    fun `test bresenham line`(from: GridPoint2, to: GridPoint2, expectedResults: Array<GridPoint2>) {
        val line = line(from, to).toTypedArray()
        assertArrayEquals(expectedResults, line)
    }

    @ParameterizedTest
    @MethodSource("typeArrayToTilesTestArgs")
    fun `test typeArrayToTiles`(size: Int, tilesTemplate: Array<Int>, expectedResults: Array<Tile>) {
        val tiles = typeArrayToTiles(tilesTemplate, size)
        val tilesSet = tiles.toSet()
        val expectedTilesSet = expectedResults.toSet()

        //todo sets deep equality
        assertTrue { expectedTilesSet.containsAll(tilesSet) }
        assertTrue { tilesSet.containsAll(expectedTilesSet) }
    }

    @ParameterizedTest
    @MethodSource("fovTestArgs")
    fun `test fieldOfView`(center: GridPoint2, radius: Int, size: Int, tilesTemplate: Array<Int>, expectedResults: Array<Int>) {
        val inFovPointId = 1
        val tiles = typeArrayToTiles(tilesTemplate, size)
        val expectedTiles = typeArrayToTiles(expectedResults, size)
        val expectedPositions = expectedTiles
            .filter { it.typeId == inFovPointId }
            .map { it.position }.toSet()

        val fov = fieldOfView(center, radius, ::lightTilePassMapper, TileArray(tiles, size)).toSet()

        //todo sets deep equality
        assertTrue { expectedPositions.containsAll(fov) }
        assertTrue { fov.containsAll(expectedPositions) }
    }
}