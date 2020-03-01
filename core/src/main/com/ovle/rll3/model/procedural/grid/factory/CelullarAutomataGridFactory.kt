package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.CelullarAutomataSettings

class CelullarAutomataGridFactory: GridFactory {

    companion object {
        const val wallMarker = 1.0f
    }

    override fun get(size: Int, settings: LevelGenerationSettings): Grid {
        settings as CelullarAutomataSettings

        val grid = Grid(size)

        val generator = CellularAutomataGenerator.getInstance()
        generator.apply {
            marker = wallMarker
            deathLimit = 2
            birthLimit = 6
            setInitiate(false)
            //todo
        }
        CellularAutomataGenerator.initiate(grid, generator)
        init(grid, size)

        generator.generate(grid)

        return grid
    }

    private fun init(grid: Grid, size: Int) {
        for (x in (0 until size)) {
            grid.set(x, 0, wallMarker)
            grid.set(x, size - 1, wallMarker)
        }
        for (y in (0 until size)) {
            grid.set(0, y, wallMarker)
            grid.set(size - 1, y, wallMarker)
        }
    }
}