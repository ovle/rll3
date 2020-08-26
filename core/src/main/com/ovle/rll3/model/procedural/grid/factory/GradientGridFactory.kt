package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.config.LevelFactoryParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.generator.GradientMapGenerator

class GradientGridFactory(val params: LevelFactoryParams.GradientLevelFactoryParams): GridFactory {

    override fun get(random: RandomParams): Grid {
        val result = Grid(params.size)

        val mapGenerator = GradientMapGenerator()
        mapGenerator.generate(result)

        return result
    }
}