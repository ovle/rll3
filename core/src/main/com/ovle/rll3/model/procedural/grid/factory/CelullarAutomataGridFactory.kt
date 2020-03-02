package com.ovle.rll3.model.procedural.grid.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.cellular.CellularAutomataGenerator
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings
import com.ovle.rll3.model.procedural.config.LevelGenerationSettings.CelullarAutomataSettings

class CelullarAutomataGridFactory: GridFactory {

    companion object {
        const val wallMarker = 1.0f
        const val pitMarker = 0.5f
    }

    override fun get(size: Int, settings: LevelGenerationSettings): Grid {
        settings as CelullarAutomataSettings

        val wallGrid = Grid(size)

        val generator = CellularAutomataGenerator.getInstance()
        generator.apply {
            marker = wallMarker
            deathLimit = 2
            birthLimit = 5
            setInitiate(false)
        }

        CellularAutomataGenerator.initiate(wallGrid, generator)
        init(wallGrid, size, wallMarker)
        generator.generate(wallGrid)

        val pitGrid = Grid(size)
        generator.apply {
            marker = pitMarker
            deathLimit = 3
            birthLimit = 6
        }
        CellularAutomataGenerator.initiate(pitGrid, generator)
        generator.generate(pitGrid)

        return merge(arrayOf(wallGrid, pitGrid), size)
    }

    //todo
    private fun merge(grids: Array<Grid>, size: Int): Grid {
        val emptyTileId = 0.0f
        val result = Grid(size)
        for (x in (0 until size)) {
            for (y in (0 until size)) {
                val value = grids.map { it.get(x, y) }.findLast { it > emptyTileId }
                    ?: continue
                result.set(x, y, value)
            }
        }
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