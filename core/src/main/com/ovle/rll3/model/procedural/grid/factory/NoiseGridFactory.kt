package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator
import com.ovle.rll3.model.procedural.config.LevelFactoryParams

class NoiseGridFactory: GridFactory {

    companion object {
        const val wallTreshold = 0.8f
        const val floorTreshold = 0.5f
    }

    override fun get(factoryParams: LevelFactoryParams): Grid {
        factoryParams as LevelFactoryParams.NoiseLevelFactoryParams

        val size = factoryParams.size
        val result = Grid(size)

        val generator = NoiseGenerator.getInstance()
        generator.apply {
            radius = factoryParams.radius
            modifier = factoryParams.modifier
        }

        generator.generate(result)
//        connect(result, CelullarAutomataGridFactory.emptyTileMarker, CelullarAutomataGridFactory.wallMarker, factoryParams.connectionStrategy)

        return result
    }
}