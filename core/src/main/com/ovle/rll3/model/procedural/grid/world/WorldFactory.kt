package com.ovle.rll3.model.procedural.grid.world

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rlUtil.RandomParams
import com.ovle.rlUtil.TileArray
import com.ovle.rll3.model.procedural.config.WorldGenerationParams
import com.ovle.rll3.model.procedural.config.world.deepWaterTileId
import com.ovle.rll3.model.procedural.config.world.highMountainTileId
import com.ovle.rll3.model.procedural.config.world.lowMountainTileId
import com.ovle.rll3.model.procedural.config.world.shallowWaterTileId
import com.ovle.rlUtil.gdx.math.Area
import com.ovle.rlUtil.gdx.math.component1
import com.ovle.rlUtil.gdx.math.component2
import com.ovle.rlUtil.noise4j.grid.floodFill
import com.ovle.rlUtil.noise4j.grid.gridToTileArray
import com.ovle.rlUtil.noise4j.grid.normalize
import com.ovle.rll3.model.util.randomId


class WorldFactory(val params: WorldGenerationParams) {

    private val areaGroups = arrayOf(
        setOf(highMountainTileId.toFloat(), lowMountainTileId.toFloat()),
        setOf(deepWaterTileId.toFloat(), shallowWaterTileId.toFloat())
    )

    fun get(random: RandomParams): WorldInfo {
        val heightMap = params.heightMapFactory.get(random).apply { normalize(this) }
        val heatMap = params.heatMapFactory.get(random)
        val tiles = gridToTileArray(heightMap, heatMap, params.tileMapper)
        val areas = areas(tiles)

        val result = WorldInfo(
            id = randomId(),
            name = "test",
            random = random,
//            params = params,
            tiles = tiles,
            heightGrid = heightMap,
            heatGrid = heatMap,
            areas = areas.map { WorldArea(areaName(it, random), it) }
        )

        params.postProcessors.forEach {
            it.process(result)
        }

        return result
    }

    //todo
    private fun areaName(area: Area, random: RandomParams): String {
        return "area${random.kRandom.nextInt()}"
    }

    private fun areas(tiles: TileArray): Collection<Area> {
        val result = mutableListOf<Area>()
        val grid = toGrid(tiles)
        val allPoints = tiles.points().toMutableList()

        while (allPoints.isNotEmpty()) {
            val (x, y) = allPoints.first()
            val tileValue = grid[x, y]
            val areaCheck: (Float) -> Boolean = { it == tileValue || isInSameGroup(it, tileValue) }
            val area = floodFill(x, y, grid, areaCheck)
            result.add(area)

            allPoints.removeAll(area.points)
        }

        return result
    }

    private fun isInSameGroup(value1: Float, value2: Float): Boolean {
        val group = areaGroups.find { value1 in it } ?: return false
        return value2 in group
    }

    private fun toGrid(tiles: TileArray) = Grid(tiles.data.map { it.toFloat() }.toFloatArray(), tiles.size, tiles.size)
}