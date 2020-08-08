package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.procedural.config.LevelFactoryParams
import com.ovle.rll3.model.procedural.grid.generator.FractalMapGenerator
import java.util.*

class FractalGridFactory: GridFactory {

    companion object {
        const val wallTreshold = 0.8f
        const val floorTreshold = 0.5f
    }

    override fun get(factoryParams: LevelFactoryParams, worldInfo: WorldInfo): Grid {
        factoryParams as LevelFactoryParams.FractalLevelFactoryParams

        val size = factoryParams.size
        val result = Grid(size)

        val g = FractalMapGenerator(r = Random(worldInfo.seed))
        g.generate(result)

        return result
    }
}