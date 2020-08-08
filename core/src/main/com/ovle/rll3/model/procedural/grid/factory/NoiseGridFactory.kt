package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator
import com.ovle.rll3.Seed
import com.ovle.rll3.model.procedural.config.LevelFactoryParams

class NoiseGridFactory: GridFactory {

    companion object {
        const val wallTreshold = 0.8f
        const val floorTreshold = 0.5f
    }

    override fun get(params: LevelFactoryParams, seed: Seed): Grid {
        params as LevelFactoryParams.NoiseLevelFactoryParams

        val size = params.size
        val result = Grid(size)

        val generator = NoiseGenerator.getInstance()
        generator.apply {
            this.seed = seed.toInt()
            radius = params.radius
            modifier = params.modifier
        }

        generator.generate(result)

        return result
    }
}