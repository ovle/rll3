package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.Seed
import com.ovle.rll3.model.procedural.config.LevelFactoryParams
import com.ovle.rll3.model.procedural.grid.generator.FractalMapGenerator
import java.util.*

class FractalGridFactory: GridFactory {

    companion object {
        const val wallTreshold = 0.8f
        const val floorTreshold = 0.5f
    }

    override fun get(params: LevelFactoryParams, seed: Seed): Grid {
        params as LevelFactoryParams.FractalLevelFactoryParams

        val size = params.size
        val result = Grid(size)

        val g = FractalMapGenerator(r = Random(seed))
        g.generate(result)

        return result
    }
}