package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator
import com.github.czyzby.noise4j.map.generator.util.Generators
import com.ovle.rll3.model.procedural.config.GridFactoryParams.CelullarAutomataGridFactoryParams
import com.ovle.rll3.model.procedural.config.RandomParams
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.util.connect


class CelullarAutomataGridFactory(val params: CelullarAutomataGridFactoryParams): GridFactory {

    override fun get(random: RandomParams): Grid {
        val size = params.size
        val result = Grid(size)

        val generator = CellularAutomataGenerator.getInstance()
        Generators.setRandom(random.jRandom)
        generator.apply {
            radius = params.radius
            iterationsAmount = params.iterationsAmount
            marker = params.lifeCellMarker
            deathLimit = params.deathLimit
            birthLimit = params.birthLimit
            aliveChance = 1.0f - params.aliveChance // this parameter is actually reversed in generator
        }

        generator.generate(result)
        params.connectionStrategy?.let {
            connect(result, params.deadCellMarker, params.lifeCellMarker, it)
        }

        return result
    }
}