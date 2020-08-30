package com.ovle.rll3.model.procedural.grid.world

import com.ovle.rll3.model.module.core.entity.randomId
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.config.WorldGenerationParams
import com.ovle.rll3.model.procedural.grid.util.normalize
import com.ovle.rll3.model.util.gridToTileArray


class WorldFactory(val params: WorldGenerationParams) {

    fun get(random: RandomParams): WorldInfo {
        val heightMap = params.heightMapFactory.get(random)
        normalize(heightMap)
        val heatMap = params.heatMapFactory.get(random)
        val tiles = gridToTileArray(heightMap, heatMap, params.tileMapper)

        val result = WorldInfo(
            id = randomId(),
            name = "test",
            random = random,
            params = params,
            tiles = tiles,
            heightGrid = heightMap,
            heatGrid = heatMap
        )

        params.postProcessors.forEach {
            it.process(result)
        }

        return result
    }
}