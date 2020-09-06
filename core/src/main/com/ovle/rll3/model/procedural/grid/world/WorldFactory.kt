package com.ovle.rll3.model.procedural.grid.world

import com.ovle.rll3.TileArray
import com.ovle.rll3.model.module.core.entity.randomId
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.WorldGenerationParams
import com.ovle.rll3.model.util.Area
import com.ovle.rll3.model.util.floodFill
import com.ovle.rll3.model.util.gridToTileArray
import com.ovle.rll3.model.util.normalize
import com.ovle.rll3.toGrid


class WorldFactory(val params: WorldGenerationParams) {

    fun get(random: RandomParams): WorldInfo {
        val heightMap = params.heightMapFactory.get(random).apply { normalize(this) }
        val heatMap = params.heatMapFactory.get(random)
        val tiles = gridToTileArray(heightMap, heatMap, params.tileMapper)
        val areas = areas(tiles)

        val result = WorldInfo(
            id = randomId(),
            name = "test",
            random = random,
            params = params,
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
        val grid = tiles.toGrid()
        val allPoints = tiles.points().toMutableList()

        while (allPoints.isNotEmpty()) {
            val p = allPoints.first()
            val t = grid[p.x, p.y]
            val area = floodFill(p.x, p.y, grid) { it == t }
            result.add(area)

            allPoints.removeAll(area.points)
        }

        return result
    }
}