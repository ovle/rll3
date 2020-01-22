package com.ovle.rll3.model.lightning.other

import com.ovle.rll3.model.ecs.component
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PositionComponent
import com.ovle.rll3.model.ecs.entityWith
import com.ovle.rll3.model.tile.Tile
import com.ovle.rll3.model.tile.TileArray
import com.ovle.rll3.model.util.discretization.bresenham.filledCircle
import com.ovle.rll3.model.util.lightTilePassMapper
import com.ovle.rll3.model.util.lineOfSight.rayTracing.cropArea
import com.ovle.rll3.model.util.lineOfSight.rayTracing.obstacles
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class Test {

    companion object {
        @JvmStatic
        fun args() = testCases
    }

    @ParameterizedTest
    @MethodSource("args")
    fun `test lightning`(tileTemplate: TileTemplate, entitiesData: EntitiesData, expectedResults: ExpectedResult) {
        val entities = entitiesData.entities.toList()
        val lightSourceEntity = entityWith(entities, LightComponent::class)
        val light = lightSourceEntity.component(LightComponent::class)!!
        val lightPosition = lightSourceEntity.component(PositionComponent::class)!!

        val area = filledCircle(lightPosition.position, light.radius)
        assertEquals(expectedResults.areaSize, area.size, "area size")

        val size = tileTemplate.size
        val tiles = TileArray(tileTemplate.data.map { Tile(it) }.toTypedArray(), size)

        val croppedArea = cropArea(area, tiles)
        assertEquals(expectedResults.croppedAreaSize, croppedArea.size, "cropped area size")

        val obstacles = obstacles(croppedArea, ::lightTilePassMapper, tiles)
        assertEquals(expectedResults.obstaclesCount, obstacles.size, "obstacles count")
    }
}