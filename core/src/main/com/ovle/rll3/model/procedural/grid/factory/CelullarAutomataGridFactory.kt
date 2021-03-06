package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator
import com.github.czyzby.noise4j.map.generator.util.Generators
import com.ovle.rll3.model.ecs.component.special.WorldInfo
import com.ovle.rll3.model.procedural.config.LevelFactoryParams
import com.ovle.rll3.model.procedural.config.LevelFactoryParams.CelullarAutomataLevelFactoryParams
import com.ovle.rll3.model.procedural.grid.utils.connect
import java.util.*

class CelullarAutomataGridFactory: GridFactory {

    companion object {
        const val wallMarker = 1.0f
        const val pitMarker = 0.5f
        const val emptyTileMarker = 0.0f
    }

    override fun get(factoryParams: LevelFactoryParams, worldInfo: WorldInfo): Grid {
        factoryParams as CelullarAutomataLevelFactoryParams

        val size = factoryParams.size
        val result = Grid(size)

        val generator = CellularAutomataGenerator.getInstance()
        Generators.setRandom(Random(worldInfo.seed))
        generator.apply {
            marker = wallMarker
            deathLimit = 2  //more will kill the walls
            birthLimit = 4
            aliveChance = 0.6f
            setInitiate(false) //will do it manually
        }

        CellularAutomataGenerator.initiate(result, generator)
        init(result, size, wallMarker)
        generator.generate(result)
        connect(result, emptyTileMarker, wallMarker, factoryParams.connectionStrategy)

        return result
    }

    private fun init(grid: Grid, size: Int, marker: Float) {
        for (x in (0 until size)) {
            grid.set(x, 0, marker)
            grid.set(x, size - 1, marker)
        }
        for (y in (0 until size)) {
            grid.set(0, y, marker)
            grid.set(size - 1, y, marker)
        }
    }
}