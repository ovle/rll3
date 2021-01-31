package com.ovle.rll3.model.lightning.other


class Test {

    companion object {
        @JvmStatic
        fun args() = testCases
    }

//    @ParameterizedTest
//    @MethodSource("args")
//    fun `test lightning`(tileTemplate: TileTemplate, entitiesData: EntitiesData, expectedResults: ExpectedResult) {
//        val entities = entitiesData.entities.toList()
//        val lightSourceEntity = entityWith(entities, AOEData::class)
//        val light = lightSourceEntity?.get(light)!!
//        val lightPosition = lightSourceEntity?.get(position)!!
//
//        val area = filledCircle(lightPosition.gridPosition, light.radius)
//        assertEquals(expectedResults.areaSize, area.size, "area size")
//
//        val size = tileTemplate.size
//        val tiles = TileArray(tileTemplate.data.map { Tile(it) }.toTypedArray(), size)
//
//        val croppedArea = cropArea(area, tiles)
//        assertEquals(expectedResults.croppedAreaSize, croppedArea.size, "cropped area size")
//
//        val obstacles = obstacles(croppedArea, ::lightTilePassMapper, tiles)
//        assertEquals(expectedResults.obstaclesCount, obstacles.size, "obstacles count")
//    }
}