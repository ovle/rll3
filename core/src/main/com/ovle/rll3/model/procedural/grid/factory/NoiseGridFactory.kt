package com.ovle.rll3.model.procedural.grid.factory

//import com.github.czyzby.noise4j.map.Grid
//import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator
//import com.ovle.rll3.Seed
//import com.ovle.rll3.model.procedural.config.LevelFactoryParams
//import com.ovle.rll3.model.procedural.config.RandomParams
//
//class NoiseGridFactory: GridFactory {
//
//    override fun get(params: LevelFactoryParams, random: RandomParams): Grid {
//        params as LevelFactoryParams.NoiseLevelFactoryParams
//
//        val size = params.size.random(random.kRandom)
//        val result = Grid(size)
//
//        val generator = NoiseGenerator.getInstance()
//        generator.apply {
//            this.seed = random.seed.toInt()
//            radius = params.radius
//            modifier = params.modifier
//        }
//
//        generator.generate(result)
//
//        return result
//    }
//}