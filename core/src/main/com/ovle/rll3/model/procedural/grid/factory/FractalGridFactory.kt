package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.FractalLevelFactoryParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.generator.FractalMapGenerator
import com.ovle.rll3.model.procedural.grid.generator.GradientPathMapGenerator

class FractalGridFactory(val params: FractalLevelFactoryParams): GridFactory {

    override fun get(random: RandomParams): Grid {
        val result = Grid(params.size)

        val mapGenerator = FractalMapGenerator(
            random = random.jRandom
        )
        mapGenerator.generate(result)

        //todo test
        val gg = GradientPathMapGenerator(random = random.jRandom)
        gg.generate(result)

        return result
    }
}